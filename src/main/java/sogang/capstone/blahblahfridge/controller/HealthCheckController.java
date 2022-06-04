package sogang.capstone.blahblahfridge.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sogang.capstone.blahblahfridge.config.CommonResponse;

@Log
@Controller
@RequestMapping("/health")
@RequiredArgsConstructor

public class HealthCheckController {

    @GetMapping(value = "/")
    @ResponseBody
    public CommonResponse healthCheck() {
        return CommonResponse.onSuccess(null);
    }
}
