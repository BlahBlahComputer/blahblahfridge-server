package sogang.capstone.blahblahfridge.controller;

import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.TokenDTO;
import sogang.capstone.blahblahfridge.dto.oauth.NaverTokenDTO;
import sogang.capstone.blahblahfridge.dto.oauth.NaverUserDTO;
import sogang.capstone.blahblahfridge.exception.BadRequestException;
import sogang.capstone.blahblahfridge.persistence.UserRepository;
import sogang.capstone.blahblahfridge.service.JwtTokenServiceImpl;

@Log
@Controller
@RequestMapping("/naver")
@RequiredArgsConstructor
@PropertySource("classpath:/application.properties")
public class NaverController {

    private final UserRepository repo;
    private final WebClient webClient;
    private final JwtTokenServiceImpl jwtTokenServiceImpl;

    @Value("${naver.key}")
    private String NAVER_KEY;
    @Value("${naver.secret}")
    private String NAVER_SECRET;
    @Value("${naver.uri}")
    private String NAVER_URI;

    @PostMapping(value = "/login", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse postNaverLogin(@RequestParam("code") String code,
        @RequestParam("state") String state) {

        String getTokenURL =
            "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id="
                + NAVER_KEY + "&client_secret=" + NAVER_SECRET + "&redirect_uri=" + NAVER_URI
                + "&code=" + code + "&state=" + state;

        NaverTokenDTO naverTokenDTO = (NaverTokenDTO) webClient.post().uri(getTokenURL).retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                clientResponse -> Mono.error(new BadRequestException("잘못된 요청입니다.")))
            .bodyToMono(
                ParameterizedTypeReference.forType(NaverTokenDTO.class))
            .block();

        String getUserURL = "https://openapi.naver.com/v1/nid/me";

        NaverUserDTO naverUserDTO = (NaverUserDTO) webClient.post().uri(getUserURL)
            .header("Authorization", "Bearer " + naverTokenDTO.getAccessToken())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                clientResponse -> Mono.error(new BadRequestException("잘못된 요청입니다.")))
            .bodyToMono(
                ParameterizedTypeReference.forType(NaverUserDTO.class))
            .block();

        Optional<User> user = repo.findByAuthenticationCode(
            naverUserDTO.getResponse().getAuthenticationCode());
        if (user.isEmpty()) {
            return CommonResponse.onSuccess(naverTokenDTO);
        }

        TokenDTO tokenDTO = new TokenDTO(user.get().getId());
        return CommonResponse.onSuccess(jwtTokenServiceImpl.encodeJwtToken(tokenDTO));
    }

    @PostMapping(value = "/register", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse postNaverRegister(
        @Valid @RequestBody NaverTokenDTO naverTokenDTO) {

        String getUserURL = "https://openapi.naver.com/v1/nid/me";

        NaverUserDTO naverUserDTO = (NaverUserDTO) webClient.post().uri(getUserURL)
            .header("Authorization", "Bearer " + naverTokenDTO.getAccessToken())
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                clientResponse -> Mono.error(new BadRequestException("잘못된 요청입니다.")))
            .bodyToMono(
                ParameterizedTypeReference.forType(NaverUserDTO.class))
            .block();

        Optional<User> checkUser = repo.findByAuthenticationCode(
            naverUserDTO.getResponse().getAuthenticationCode());
        if (checkUser.isPresent()) {
            return CommonResponse.onFailure(HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다."); // 다시 하세요~!
        }
        Optional<User> checkUserNickName = repo.findByAuthenticationCode(
            naverUserDTO.getResponse().getAuthenticationCode());
        if (checkUser.isPresent()) {
            return CommonResponse.onFailure(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."); // 다시 하세요~!
        }

        User newUser = User.builder()
            .username(naverUserDTO.getResponse().getNickname())
            .image(naverUserDTO.getResponse().getImageUrl())
            .authenticationCode(naverUserDTO.getResponse().getAuthenticationCode())
            .provider("naver")
            .build();
        repo.save(newUser);

        TokenDTO tokenDTO = new TokenDTO(newUser.getId());
        return CommonResponse.onSuccess(jwtTokenServiceImpl.encodeJwtToken(tokenDTO));
    }

}
