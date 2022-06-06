package sogang.capstone.blahblahfridge.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.request.AnalyzeRequest;
import sogang.capstone.blahblahfridge.controller.request.ReviewRequest;
import sogang.capstone.blahblahfridge.domain.Ingredient;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.MenuIngredient;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.AIImageDTO;
import sogang.capstone.blahblahfridge.dto.AnalyzeResultDTO;
import sogang.capstone.blahblahfridge.dto.MenuDTO;
import sogang.capstone.blahblahfridge.dto.MenuIngredientDTO;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.dto.ReviewImageDTO;
import sogang.capstone.blahblahfridge.dto.TokenDTO;
import sogang.capstone.blahblahfridge.dto.oauth.NaverTokenDTO;
import sogang.capstone.blahblahfridge.dto.oauth.NaverUserDTO;
import sogang.capstone.blahblahfridge.exception.BadRequestException;
import sogang.capstone.blahblahfridge.persistence.IngredientRepository;
import sogang.capstone.blahblahfridge.persistence.MenuIngredientRepository;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;

@Log
@Controller
@RequestMapping("menu")
@AllArgsConstructor
public class MenuController {

    MenuRepository repo;
    MenuIngredientRepository miRepo;
    ReviewRepository rRepo;
    IngredientRepository iRepo;
    AmazonS3 s3Client;
    WebClient webClient;

    @GetMapping(produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<MenuDTO>> findAllMenu(
        @RequestParam(value = "name", required = false) String name) {
        if (name != null) {
            List<Menu> menuList = repo.findAllByNameContaining(name);
            List<MenuDTO> menuDTOList = menuList.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
            return CommonResponse.onSuccess(menuDTOList);
        } else {
            List<Menu> menuList = repo.findAll();
            List<MenuDTO> menuDTOList = menuList.stream()
                .map(MenuDTO::new)
                .collect(Collectors.toList());
            return CommonResponse.onSuccess(menuDTOList);
        }
    }


    @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<MenuDTO> getMenuById(@PathVariable("id") Long id) {
        Optional<Menu> menu = repo.findById(id);
        if (menu.isEmpty()) {
            return CommonResponse.onFailure(HttpStatus.NOT_FOUND, "해당 메뉴가 없습니다.");
        }

        MenuDTO menuDTO = new MenuDTO(menu.get());
        return CommonResponse.onSuccess(menuDTO);
    }

    @GetMapping(value = "/{id}/ingredient", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<MenuIngredientDTO>> getMenuIngredientByMenuId(
        @PathVariable("id") Long id) {
        List<MenuIngredient> menuIngredientList = miRepo.findAllByMenuId(id);
        List<MenuIngredientDTO> menuIngredientDTOList = menuIngredientList.stream()
            .map(MenuIngredientDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(menuIngredientDTOList);
    }

    @GetMapping(value = "/{id}/review", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<ReviewDTO>> getReviewByMenuId(@PathVariable("id") Long id) {
        List<Review> reviewList = rRepo.findAllByMenuId(id);
        List<ReviewDTO> reviewDTOList = reviewList.stream()
            .map(ReviewDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(reviewDTOList);
    }

    @GetMapping(value="/upload", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<AIImageDTO> uploadImage() {
        ZonedDateTime expiredDate = ZonedDateTime.now().plusHours(1);
        UUID randomFileName = UUID.randomUUID();

        URI uri;
        try {
            uri = this.s3Client.generatePresignedUrl("blahblah-image",
                randomFileName.toString(), Date.from(expiredDate.toInstant()), HttpMethod.POST).toURI();
        } catch(URISyntaxException e) {
            throw new BadRequestException("파일 URL 생성중 오류가 발생했습니다.");
        }

        String presignedURL = uri.toString();
        String imageURL = "https://ai-image.blahblahfridge.site/" + randomFileName.toString();

        AIImageDTO result = AIImageDTO.builder()
            .presignedURL(presignedURL)
            .imageURL(imageURL)
            .build();

        return CommonResponse.onSuccess(result);
    }

    @PostMapping(value="/analyze", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse postImageAnalyze(@Valid @RequestBody AnalyzeRequest analyzeRequest) {
        String analyzeURL =
            "https://ai.blahblahfridge.site/analyze";
        Map<String, String> bodyMap = new HashMap();
        bodyMap.put("bucket", analyzeRequest.getBucket());
        bodyMap.put("key", analyzeRequest.getKey());

        AnalyzeResultDTO analyzeResultDTO = (AnalyzeResultDTO) webClient.post().uri(analyzeURL)
            .body(BodyInserters.fromValue(bodyMap)).retrieve()
            .onStatus(HttpStatus::is4xxClientError,
                clientResponse -> Mono.error(new BadRequestException("잘못된 요청입니다.")))
            .bodyToMono(
                ParameterizedTypeReference.forType(AnalyzeResultDTO.class))
            .block();

        List<String> ingredientNameList = analyzeResultDTO.getRes();
        List<Ingredient> ingredientList = iRepo.findAllByNameIn(ingredientNameList);
        List<Long> ingredientIdList = ingredientList.stream().map(Ingredient::getId).collect(
            Collectors.toList());
        List<MenuIngredient> menuIngredientList = miRepo.findAllByIngredientIdIn(ingredientIdList);
        List<Menu> menuList = menuIngredientList.stream().map(MenuIngredient::getMenu)
            .collect(Collectors.toList());

        List<MenuDTO> menuDTOList = menuList.stream().map(MenuDTO::new).collect(Collectors.toList());

        return CommonResponse.onSuccess(menuDTOList);
    }
}