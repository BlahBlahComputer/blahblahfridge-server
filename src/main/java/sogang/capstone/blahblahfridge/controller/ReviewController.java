package sogang.capstone.blahblahfridge.controller;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.request.ReviewRequest;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.dto.ReviewImageDTO;
import sogang.capstone.blahblahfridge.dto.ReviewResponseDTO;
import sogang.capstone.blahblahfridge.exception.BadRequestException;
import sogang.capstone.blahblahfridge.persistence.MenuRepository;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

@Log
@Controller
@RequestMapping("review")
@AllArgsConstructor
public class ReviewController {

    ReviewRepository repo;
    UserRepository uRepo;
    MenuRepository mRepo;
    AmazonS3 s3Client;

    @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<ReviewResponseDTO> getReviewById(@AuthenticationPrincipal User authUser,
        @PathVariable("id") Long id) {
        Optional<Review> review = repo.findById(id);
        if (review.isEmpty()) {
            return CommonResponse.onFailure(HttpStatus.NOT_FOUND, "?????? ????????? ????????????.");
        }
        boolean deletable = false;
        if (review.get().getUser().getId() == authUser.getId()) {
            deletable = true;
        }
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO(review.get(), deletable);
        return CommonResponse.onSuccess(reviewResponseDTO);
    }

    @PostMapping(produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<ReviewDTO> postReview(@AuthenticationPrincipal User authUser,
        @Valid @RequestBody ReviewRequest reviewRequest) {

        Optional<Menu> menu = mRepo.findById(reviewRequest.getMenuId());
        if (menu.isEmpty()) {
            return CommonResponse.onFailure(HttpStatus.NOT_FOUND, "?????? ????????? ????????????.");
        }
        Optional<User> user = uRepo.findById(authUser.getId());
        if (user.isEmpty()) {
            return CommonResponse.onFailure(HttpStatus.NOT_FOUND, "?????? ????????? ????????????.");
        }

        Review review = Review.builder()
            .rate(reviewRequest.getRate())
            .content(reviewRequest.getContent())
            .image(reviewRequest.getImage())
            .user(user.get())
            .menu(menu.get())
            .build();
        repo.save(review);

        ReviewDTO reviewDTO = new ReviewDTO(review);
        return CommonResponse.onSuccess(reviewDTO);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse deleteReviewById(@AuthenticationPrincipal User authUser,
        @PathVariable("id") Long id) {
        Optional<Review> review = repo.findById(id);
        if (review.isEmpty()) {
            return CommonResponse.onFailure(HttpStatus.NOT_FOUND, "?????? ????????? ????????????.");
        }
        if (review.get().getUser().getId() != authUser.getId()) {
            return CommonResponse.onFailure(HttpStatus.NOT_FOUND, "?????? ????????? ????????? ????????????.");
        }
        repo.delete(review.get());

        return CommonResponse.onSuccess(null);
    }

    @GetMapping(value="/upload", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<ReviewImageDTO> uploadImage() {
        ZonedDateTime expiredDate = ZonedDateTime.now().plusHours(1);
        UUID randomFileName = UUID.randomUUID();

        URI uri;
        try {
            uri = this.s3Client.generatePresignedUrl("blahblah-review",
                randomFileName.toString(), Date.from(expiredDate.toInstant()), HttpMethod.PUT).toURI();
        } catch(NullPointerException e) {
            return CommonResponse.onFailure(HttpStatus.BAD_REQUEST, "?????? URL ????????? ????????? ??????????????????.");
        } catch(URISyntaxException e) {
            return CommonResponse.onFailure(HttpStatus.BAD_REQUEST, "?????? URL ????????? ????????? ??????????????????.");
        }

        String presignedURL = uri.toString();
        String imageURL = "https://review-image.blahblahfridge.site/" + randomFileName.toString();

        ReviewImageDTO result = ReviewImageDTO.builder()
            .presignedURL(presignedURL)
            .imageURL(imageURL)
            .build();

        return CommonResponse.onSuccess(result);
    }
}
