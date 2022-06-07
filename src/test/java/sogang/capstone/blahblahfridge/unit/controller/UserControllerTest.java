package sogang.capstone.blahblahfridge.unit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.UserController;
import sogang.capstone.blahblahfridge.controller.request.UserImageRequest;
import sogang.capstone.blahblahfridge.controller.request.UserNameRequest;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuCategory;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.dto.UserDTO;
import sogang.capstone.blahblahfridge.dto.UserReviewDTO;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

public class UserControllerTest {

    @Test
    @DisplayName("유저 조회 시 없을 경우, 에러 처리 되는지 확인")
    public void testIfUserNotExistThenThrowException() {
        // given
        User user1 = User.builder().id(1L).username("유저1").authenticationCode("123")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );

        // then
        CommonResponse result = userController.getUserById(user1);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."),
            result);
    }

    @Test
    @DisplayName("유저 조회 시 있을 경우, 결과 나오는지 확인")
    public void testIfUserExistThenReturnUser() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user));
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );
        CommonResponse<UserDTO> result = userController.getUserById(user);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new UserDTO(user)), result);
    }

    @Test
    @DisplayName("유저 이미지 수정 시 유저 없을 경우, 예외 처리 되는지 확인")
    public void testIfUserImageEditFailWhenUserNotExistThenThrowException() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        UserImageRequest userImageRequest = new UserImageRequest("image");
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );

        // then
        CommonResponse result = userController.editUserImageById(user, userImageRequest);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."),
            result);
    }

    @Test
    @DisplayName("유저 이미지 수정 성공 시, 수정 결과 반환 되는지 확인")
    public void testIfUserImageEditSuccessThenReturnEditedUser() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user));
        UserImageRequest userImageRequest = new UserImageRequest("new image");
        user.setImage(userImageRequest.getImage());
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );
        CommonResponse<UserDTO> result = userController.editUserImageById(user, userImageRequest);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new UserDTO(user)), result);
    }

    @Test
    @DisplayName("유저 이름 수정 시 유저 없을 경우, 예외 처리 되는지 확인")
    public void testIfUserNameEditFailWhenUserNotExistThenThrowException() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        UserNameRequest userNameRequest = new UserNameRequest("name");
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );

        // then
        CommonResponse result = userController.editUserNameById(user, userNameRequest);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."),
            result);
    }

    @Test
    @DisplayName("유저 이름 수정 성공 시, 수정 결과 반환 되는지 확인")
    public void testIfUserNameEditSuccessThenReturnEditedUser() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user));
        UserNameRequest userNameRequest = new UserNameRequest("new image");
        user.setUsername(userNameRequest.getUsername());
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );
        CommonResponse<UserDTO> result = userController.editUserNameById(user, userNameRequest);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new UserDTO(user)), result);
    }

    @Test
    @DisplayName("유저 리뷰 조회 시 유저 없을 경우, 예외 처리 되는지 확인")
    public void testIfUserReviewGetFailWhenUserNotExistThenThrowException() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(null));
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );

        // then
        CommonResponse result = userController.getUserReviewById(user);
        Assertions.assertEquals(CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."),
            result);
    }

    @Test
    @DisplayName("유저 리뷰 조회 시 결과 없으면, 빈 리스트 반환 되는지 확인")
    public void testIfUserReviewNotExistThenReturnEmptyList() {
        // given
        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user));
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findAllByUserId(1L))
            .thenReturn(new ArrayList<>());

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );
        CommonResponse<List<UserReviewDTO>> result = userController.getUserReviewById(user);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new ArrayList<>()), result);
    }

    @Test
    @DisplayName("유저 리뷰 조회 시 결과 있으면, 리뷰 리스트 반환 되는지 확인")
    public void testIfUserReviewExistThenReturnReviewList() {
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

        User user = User.builder()
            .id(1L)
            .username("user")
            .image("image")
            .authenticationCode("asdfasdf")
            .provider("kakao")
            .build();

        Review review1 = Review.builder().rate(5).content("굿").user(user).menu(menu).build();
        Review review2 = Review.builder().rate(1).content("밷").user(user).menu(menu).build();
        List<Review> reviewList = new ArrayList<Review>();
        reviewList.add(review1);
        reviewList.add(review2);

        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenReturn(Optional.ofNullable(user));
        ReviewRepository mockReviewRepository = Mockito.mock(ReviewRepository.class);
        Mockito.when(mockReviewRepository.findAllByUserId(1L))
            .thenReturn(reviewList);

        // when
        UserController userController = new UserController(
            mockUserRepository,
            mockReviewRepository
        );
        CommonResponse<List<UserReviewDTO>> result = userController.getUserReviewById(user);

        // then
        List<UserReviewDTO> userReviewDTOList = reviewList.stream()
            .map(UserReviewDTO::new)
            .collect(Collectors.toList());

        Assertions.assertEquals(CommonResponse.onSuccess(userReviewDTOList), result);
    }
}
