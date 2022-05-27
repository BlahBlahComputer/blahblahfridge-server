package sogang.capstone.blahblahfridge.unit.controller;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.UserController;
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
}
