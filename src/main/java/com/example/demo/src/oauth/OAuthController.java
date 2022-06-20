package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OAuthService oAuthService;
    private final JwtService jwtService;

    @Autowired
    public OAuthController(OAuthService oAuthService, JwtService jwtService) {
        this.oAuthService = oAuthService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<String> kakaoLogIn(@RequestParam("code") String code) {
        if(code == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        try {
            return new BaseResponse<>(oAuthService.kakaoLogIn(code));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
