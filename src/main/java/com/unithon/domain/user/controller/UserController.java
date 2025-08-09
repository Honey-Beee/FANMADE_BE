package com.unithon.domain.user.controller;

import com.unithon.domain.user.dto.UserDTO;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import com.unithon.domain.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse<String> signUp(@RequestBody UserDTO.SignUpRequest request) {
        userService.signUp(request.email, request.password);
        return BaseResponse.onSuccess(SuccessStatus.USER_SIGNUP_SUCCESS, null);
    }

    @PostMapping("/login")
    public BaseResponse<UserDTO.LoginResponse> login(@RequestBody UserDTO.LoginRequest request) {
        UserDTO.LoginResponse response = userService.login(request.email, request.password);
        return BaseResponse.onSuccess(SuccessStatus.USER_LOGIN_SUCCESS, response);
    }
}