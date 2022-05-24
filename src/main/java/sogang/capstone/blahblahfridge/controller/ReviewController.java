package sogang.capstone.blahblahfridge.controller;


import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;

@Log
@Controller
@RequestMapping("review")
@AllArgsConstructor
public class ReviewController {

    ReviewRepository repo;

    @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public ReviewDTO getReviewById(@PathVariable("id") Long id) {
        Optional<Review> review = repo.findById(id);
        if (review.isEmpty()) {
            throw new RuntimeException();
        }

        ReviewDTO reviewDTO = new ReviewDTO(review.get());
        return reviewDTO;
    }
}
