package sogang.capstone.blahblahfridge.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.TokenDTO;
import sogang.capstone.blahblahfridge.dto.oauth.KakaoTokenDTO;
import sogang.capstone.blahblahfridge.dto.oauth.KakaoUserDTO;
import sogang.capstone.blahblahfridge.exception.BadRequestException;
import sogang.capstone.blahblahfridge.persistence.UserRepository;
import sogang.capstone.blahblahfridge.service.JwtTokenServiceImpl;

@Log
@Controller
@RequestMapping("/kakao")
@RequiredArgsConstructor
@PropertySource("classpath:/application.properties")

public class KakaoController {

    private final UserRepository repo;
    private final WebClient webClient;
    private final JwtTokenServiceImpl jwtTokenServiceImpl;
    @Value("${kakao.key}")
    private String KAKAO_KEY;
    @Value("${kakao.uri}")
    private String KAKAO_URI;

    @PostMapping(value = "/login", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse postKakaoLogin(@RequestParam("code") String code) {

        String getTokenURL =
            "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id="
                + KAKAO_KEY + "&redirect_uri=" + KAKAO_URI + "&code=" + code;

        KakaoTokenDTO kakaoTokenDTO = (KakaoTokenDTO) webClient.post().uri(getTokenURL).retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                clientResponse -> Mono.error(new BadRequestException("잘못된 요청입니다.")))
            .bodyToMono(
                ParameterizedTypeReference.forType(KakaoTokenDTO.class))
            .block();

        String getUserURL = "https://kapi.kakao.com/v2/user/me";

        KakaoUserDTO kakaoUserDTO = (KakaoUserDTO) webClient.post().uri(getUserURL)
            .header("Authorization", "Bearer " + kakaoTokenDTO.getAccessToken())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                clientResponse -> Mono.error(new BadRequestException("잘못된 요청입니다.")))
            .bodyToMono(
                ParameterizedTypeReference.forType(KakaoUserDTO.class))
            .block();

        Optional<User> user = repo.findByAuthenticationCode(kakaoUserDTO.getAuthenticationCode());
        if (user.isEmpty()) {
            User newUser = User.builder()
                .username(kakaoUserDTO.getKakaoAccount().getProfile().getNickname())
                .image(kakaoUserDTO.getKakaoAccount().getProfile().getImageUrl())
                .authenticationCode(kakaoUserDTO.getAuthenticationCode())
                .provider("kakao")
                .build();
            repo.save(newUser);
            TokenDTO tokenDTO = new TokenDTO(newUser.getId());
            return CommonResponse.onSuccess(jwtTokenServiceImpl.encodeJwtToken(tokenDTO));
        }

        TokenDTO tokenDTO = new TokenDTO(user.get().getId());
        return CommonResponse.onSuccess(jwtTokenServiceImpl.encodeJwtToken(tokenDTO));
    }
}
