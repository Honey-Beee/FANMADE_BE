package com.unithon.user.controller;

import com.unithon.user.dto.UserDTO;
import com.unithon.user.application.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO.SignUpRequest request) {
        userService.signUp(request.email, request.password);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO.LoginResponse> login(@RequestBody UserDTO.LoginRequest request) {
        return ResponseEntity.ok(userService.login(request.email, request.password));
    }
}