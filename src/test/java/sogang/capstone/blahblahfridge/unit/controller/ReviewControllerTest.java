package sogang.capstone.blahblahfridge.unit.controller;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sogang.capstone.blahblahfridge.controller.ReviewController;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuCategory;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

public class ReviewControllerTest {

    @Test
    @DisplayName("리뷰를 아이디로 검색했으나 결과가 없을 때, 예외 처리 되는지 확인")
    public void testIfReviewNotExistThenThrowException() {
        // given
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(100L))
            .thenThrow(new RuntimeException());

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository
        );

        // then
        Assertions.assertThrows(RuntimeException.class, () -> {
            reviewController.getReviewById(100L);
        });
    }

    @Test
    @DisplayName("리뷰를 아이디로 검색해서 결과가 있을 때, 결과가 나오는지 확인")
    public void testIfReviewExistThenReturnMenu() {
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

        User user1 = User.builder().username("유저1").authenticationCode("123").provider("kakao")
            .build();

        Review review = Review.builder().rate(5).content("굿").user(user1).menu(menu).build();

        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findById(1L))
            .thenReturn(Optional.ofNullable(review));

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        MenuRepository mockMenuRepository = Mockito.mock(MenuRepository.class);

        // when
        ReviewController reviewController = new ReviewController(
            mockReviewRepository,
            mockUserRepository,
            mockMenuRepository
        );
        ReviewDTO result = reviewController.getReviewById(1L);

        // then
        Assertions.assertEquals(new ReviewDTO(review), result);
    }

}
