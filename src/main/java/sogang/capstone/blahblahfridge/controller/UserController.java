package sogang.capstone.blahblahfridge.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.controller.request.UserImageRequest;
import sogang.capstone.blahblahfridge.controller.request.UserNameRequest;
import sogang.capstone.blahblahfridge.domain.Review;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.ReviewDTO;
import sogang.capstone.blahblahfridge.dto.UserDTO;
import sogang.capstone.blahblahfridge.exception.NotFoundException;
import sogang.capstone.blahblahfridge.persistence.ReviewRepository;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

@Log
@Controller
@RequestMapping("user")
@AllArgsConstructor

public class UserController {

    UserRepository repo;
    ReviewRepository rRepo;

    @GetMapping(value = "/{id}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<UserDTO> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("해당 유저가 없습니다.");
        }

        UserDTO userDTO = new UserDTO(user.get());
        return CommonResponse.onSuccess(userDTO);
    }

    @PutMapping(value = "/{id}/image", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<UserDTO> editUserImageById(@PathVariable("id") Long id,
        @Valid @RequestBody UserImageRequest userImageRequest) {
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("해당 유저가 없습니다.");
        }
        user.ifPresent(origin -> {
            origin.setImage(userImageRequest.getImage());
            repo.save(origin);
        });

        UserDTO userDTO = new UserDTO(user.get());
        return CommonResponse.onSuccess(userDTO);
    }

    @PutMapping(value = "/{id}/name", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<UserDTO> editUserNameById(@PathVariable("id") Long id,
        @Valid @RequestBody UserNameRequest userNameRequest) {
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("해당 유저가 없습니다.");
        }
        user.ifPresent(origin -> {
            origin.setUsername(userNameRequest.getUsername());
            repo.save(origin);
        });

        UserDTO userDTO = new UserDTO(user.get());
        return CommonResponse.onSuccess(userDTO);
    }

    @GetMapping(value = "/{id}/review", produces = "application/json; charset=utf-8")
    @ResponseBody
    public CommonResponse<List<ReviewDTO>> getUserReviewById(@PathVariable("id") Long id) {
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("해당 유저가 없습니다.");
        }
        List<Review> reviewList = rRepo.findAllByUserId(user.get().getId());
        List<ReviewDTO> reviewDTOList = reviewList.stream()
            .map(ReviewDTO::new)
            .collect(Collectors.toList());

        return CommonResponse.onSuccess(reviewDTOList);
    }
}
