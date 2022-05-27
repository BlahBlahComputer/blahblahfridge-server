package sogang.capstone.blahblahfridge.controller;


import java.util.Optional;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.request.ReviewRequest;
import sogang.capstone.blahblahfridge.domain.Menu;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.exception.NotFoundException;
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

    @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<ReviewDTO> getReviewById(@PathVariable("id") Long id) {
        Optional<Review> review = repo.findById(id);
        if (review.isEmpty()) {
            throw new NotFoundException("해당 리뷰가 없습니다.");
        }

        ReviewDTO reviewDTO = new ReviewDTO(review.get());
        return CommonResponse.onSuccess(reviewDTO);
    }

    @PostMapping(produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<ReviewDTO> postReview(@Valid @RequestBody ReviewRequest reviewRequest) {

        Optional<Menu> menu = mRepo.findById(reviewRequest.getMenuId());
        if (menu.isEmpty()) {
            throw new NotFoundException("해당 메뉴가 없습니다.");
        }
        Optional<User> user = uRepo.findById(reviewRequest.getUserId());
        if (user.isEmpty()) {
            throw new NotFoundException("해당 유저가 없습니다.");
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
    public CommonResponse deleteReviewById(@PathVariable("id") Long id) {
        Optional<Review> review = repo.findById(id);
        if (review.isEmpty()) {
            throw new NotFoundException("해당 리뷰가 없습니다.");
        }
        repo.delete(review.get());

        return CommonResponse.onSuccess(null);
    }

}
