package com.unithon.domain.user.controller;

import com.unithon.domain.user.dto.UserDTO;
import com.unithon.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class TestController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserDTO.MyProfile> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(
                        UserDTO.MyProfile.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .build()
                ))
                .orElse(ResponseEntity.notFound().build());
    }
}
