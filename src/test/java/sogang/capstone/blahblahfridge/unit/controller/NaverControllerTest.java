package sogang.capstone.blahblahfridge.unit.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import sogang.capstone.blahblahfridge.controller.NaverController;
import sogang.capstone.blahblahfridge.persistence.UserRepository;
import sogang.capstone.blahblahfridge.service.JwtTokenServiceImpl;

public class NaverControllerTest {

    @Test
    @DisplayName("네이버 로그인 코드 없어 실패 시, 에러 처리 되는지 확인")
    public void testIfNaverLoginWithoutCodeFailThrowNullPointerException() {
        // given
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        WebClient mockWebClient = Mockito.mock(WebClient.class);
        JwtTokenServiceImpl jwtTokenServiceImpl = Mockito.mock(JwtTokenServiceImpl.class);

        // when
        NaverController naverController = new NaverController(
            mockUserRepository,
            mockWebClient,
            jwtTokenServiceImpl
        );

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            naverController.postNaverLogin(null, null);
        });
    }

    @Test
    @DisplayName("네이버 로그인 코드 에러로 실패 시, 에러 처리 되는지 확인")
    public void testIfKakaoLoginWithWrongCodeFailThrowBadRequestException() {
        // given
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        WebClient mockWebClient = Mockito.mock(WebClient.class);
        JwtTokenServiceImpl jwtTokenServiceImpl = Mockito.mock(JwtTokenServiceImpl.class);

        // when
        NaverController naverController = new NaverController(
            mockUserRepository,
            mockWebClient,
            jwtTokenServiceImpl
        );

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            naverController.postNaverLogin("Code", "blah");
        });
    }
}
