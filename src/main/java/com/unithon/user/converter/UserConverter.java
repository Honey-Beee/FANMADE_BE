package com.unithon.user.converter;

import com.unithon.user.domain.entity.User;
import com.unithon.user.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class UserConverter {
    public static UserDTO.LoginResponse toLoginResponse(User user, String accessToken){
        return UserDTO.LoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }
}
