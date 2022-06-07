package sogang.capstone.blahblahfridge.unit.controller;

import com.amazonaws.services.s3.AmazonS3;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.ReviewController;
import sogang.capstone.blahblahfridge.controller.request.ReviewRequest;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuCategory;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.dto.ReviewResponseDTO;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

public class ReviewControllerTest {

    @Test
    @DisplayName("리뷰를 아이디로 검색했으나 결과가 없을 때, 예외 처리 되는지 확인")
    public void testIfReviewNotExistThenThrowException() {
        // given
        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(100L))
            .thenReturn(Optional.ofNullable(null));

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );

        // then
        CommonResponse result = reviewController.getReviewById(user1,100L);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다."),
            result);
    }

    @Test
    @DisplayName("리뷰를 아이디로 검색해서 결과가 있을 때, 삭제 가능한 결과가 나오는지 확인")
    public void testIfReviewExistDeletableThenReturnReview() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        Review review = Review.builder().id(1L).rate(5).content("굿").user(user1).menu(menu).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(1L))
            .thenReturn(Optional.ofNullable(review));

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );
        CommonResponse<ReviewResponseDTO> result = reviewController.getReviewById(user1,1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ReviewResponseDTO(review, true)), result);
    }

    @Test
    @DisplayName("리뷰를 아이디로 검색해서 결과가 있을 때, 삭제 불가능한 결과가 나오는지 확인")
    public void testIfReviewExistUndeletableThenReturnReview() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        User user2 = User.builder().id(2L).username("유저2").authenticationCode("456")
            .provider("kakao")
            .build();

        Review review = Review.builder().id(1L).rate(5).content("굿").user(user1).menu(menu).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(1L))
            .thenReturn(Optional.ofNullable(review));

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );
        CommonResponse<ReviewResponseDTO> result = reviewController.getReviewById(user2,1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ReviewResponseDTO(review, false)), result);
    }

    @Test
    @DisplayName("리뷰 등록 성공 시, 결과가 나오는지 확인")
    public void testIfReviewPostSuccessThenReturnReview() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        Review review = Review.builder().rate(5).content("굿").user(user1).menu(menu).build();
        ReviewRequest reviewRequest = ReviewRequest.builder()
            .rate(5).content("굿").menuId(1L).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user1));
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findById(1L))
            .thenReturn(Optional.ofNullable(menu));
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );
        CommonResponse<ReviewDTO> result = reviewController.postReview(user1, reviewRequest);

        // then
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        Mockito.verify(mockReviewRepository, Mockito.times(1)).save(captor.capture());

        Assertions.assertEquals(review, captor.getValue());
        Assertions.assertEquals(CommonResponse.onSuccess(new ReviewDTO(review)), result);
    }

    @Test
    @DisplayName("리뷰 등록 시 해당 메뉴가 없을 때, 예외 처리 되는지 확인")
    public void testIfReviewPostFailWhenMenuNotFoundThenThrowException() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        Review review = Review.builder().id(1L).rate(5).content("굿").user(user1).menu(menu).build();
        ReviewRequest reviewRequest = ReviewRequest.builder()
            .rate(5).content("굿").menuId(1L).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user1));
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );

        // then
        CommonResponse result = reviewController.postReview(user1, reviewRequest);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 메뉴가 없습니다."),
            result);
    }

    @Test
    @DisplayName("리뷰 등록 시 해당 유저가 없을 때, 예외 처리 되는지 확인")
    public void testIfReviewPostFailWhenUserNotFoundThenThrowException() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        Review review = Review.builder().id(1L).rate(5).content("굿").user(user1).menu(menu).build();
        ReviewRequest reviewRequest = ReviewRequest.builder()
            .rate(5).content("굿").menuId(1L).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findById(1L))
            .thenReturn(Optional.ofNullable(menu));
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );

        // then
        CommonResponse result = reviewController.postReview(user1, reviewRequest);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."),
            result);
    }

    @Test
    @DisplayName("리뷰 삭제 성공 시, 성공 반환 되는지 확인")
    public void testIfReviewDeleteSuccessThenReturnSuccess() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        Review review = Review.builder().id(1L).rate(5).content("굿").user(user1).menu(menu).build();
        ReviewRequest reviewRequest = ReviewRequest.builder()
            .rate(5).content("굿").menuId(1L).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(1L))
            .thenReturn(Optional.ofNullable(review));
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user1));
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        Mockito.when(mockMenuRepository.findById(1L))
            .thenReturn(Optional.ofNullable(menu));
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );
        CommonResponse result = reviewController.deleteReviewById(user1, 1L);

        // then
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        Mockito.verify(mockReviewRepository, Mockito.times(1)).delete(captor.capture());

        Assertions.assertEquals(review, captor.getValue());
        Assertions.assertEquals(CommonResponse.onSuccess(null), result);
    }

    @Test
    @DisplayName("리뷰가 없어 삭제 실패 했을 때, 예외 처리 되는지 확인")
    public void testIfReviewDeleteFailWhenReviewNotFoundThenThrowException() {
        // given
        MenuCategory menuCategory = MenuCategory.builder()
            .id(1L)
            .name("한식")
            .build();

        Menu menu = Menu.builder()
            .id(1L)
            .name("메뉴")
            .time(1)
            .recipe("레시피")
            .image("이미지")
            .menuCategory(menuCategory)
            .build();

        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        Review review = Review.builder().id(1L).rate(5).content("굿").user(user1).menu(menu).build();
        ReviewRequest reviewRequest = ReviewRequest.builder()
            .rate(5).content("굿").menuId(1L).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);
        AmazonS3 mockS3Client = Mockito.mock(AmazonS3.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository,
            mockS3Client
        );

        // then
        CommonResponse result = reviewController.deleteReviewById(user1, 1L);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다."),
            result);
    }
}
