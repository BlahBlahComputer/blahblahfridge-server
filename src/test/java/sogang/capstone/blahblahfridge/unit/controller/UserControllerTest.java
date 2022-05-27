package sogang.capstone.blahblahfridge.unit.controller;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.UserController;
import sogang.capstone.blahblahfridge.controller.request.UserImageRequest;
import sogang.capstone.blahblahfridge.controller.request.UserNameRequest;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.UserDTO;
import sogang.capstone.blahblahfridge.exception.NotFoundException;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

public class UserControllerTest {

    @Test
    @DisplayName("유저 조회 시 없을 경우, 에러 처리 되는지 확인")
    public void testIfUserNotExistThenThrowException() {
        // given
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenThrow(new NotFoundException("해당 유저가 없습니다."));

        // when
        UserController userController = new UserController(
            mockUserRepository
        );

        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            userController.getUserById(1L);
        });
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

        // when
        UserController userController = new UserController(
            mockUserRepository
        );
        CommonResponse<UserDTO> result = userController.getUserById(1L);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new UserDTO(user)), result);
    }

    @Test
    @DisplayName("유저 이미지 수정 시 유저 없을 경우, 예외 처리 되는지 확인")
    public void testIfUserImageEditFailWhenUserNotExistThenThrowException() {
        // given
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenThrow(new NotFoundException("해당 유저가 없습니다."));
        UserImageRequest userImageRequest = new UserImageRequest("image");

        // when
        UserController userController = new UserController(
            mockUserRepository
        );

        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            userController.editUserImageById(1L, userImageRequest);
        });
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

        // when
        UserController userController = new UserController(
            mockUserRepository
        );
        CommonResponse<UserDTO> result = userController.editUserImageById(1L, userImageRequest);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new UserDTO(user)), result);
    }

    @Test
    @DisplayName("유저 이름 수정 시 유저 없을 경우, 예외 처리 되는지 확인")
    public void testIfUserNameEditFailWhenUserNotExistThenThrowException() {
        // given
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        Mockito.when(mockUserRepository.findById(1L))
            .thenThrow(new NotFoundException("해당 유저가 없습니다."));
        UserNameRequest userNameRequest = new UserNameRequest("name");

        // when
        UserController userController = new UserController(
            mockUserRepository
        );

        // then
        Assertions.assertThrows(NotFoundException.class, () -> {
            userController.editUserNameById(1L, userNameRequest);
        });
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

        // when
        UserController userController = new UserController(
            mockUserRepository
        );
        CommonResponse<UserDTO> result = userController.editUserNameById(1L, userNameRequest);

        // then
        Assertions.assertEquals(CommonResponse.onSuccess(new UserDTO(user)), result);
    }

}
