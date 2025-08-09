package com.unithon.domain.user.converter;

import com.unithon.domain.user.dto.UserDTO;
import com.unithon.domain.user.domain.entity.User;

public class UserConverter {
    public static UserDTO.LoginResponse toLoginResponse(User user, String accessToken){
        return UserDTO.LoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }
}
