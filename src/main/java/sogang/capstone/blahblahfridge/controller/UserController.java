package sogang.capstone.blahblahfridge.controller;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.config.CommonResponse;
import sogang.capstone.blahblahfridge.domain.User;
import sogang.capstone.blahblahfridge.dto.UserDTO;
import sogang.capstone.blahblahfridge.exception.NotFoundException;
import sogang.capstone.blahblahfridge.persistence.UserRepository;

@Log
@Controller
@RequestMapping("user")
@AllArgsConstructor

public class UserController {

    UserRepository repo;

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

}
