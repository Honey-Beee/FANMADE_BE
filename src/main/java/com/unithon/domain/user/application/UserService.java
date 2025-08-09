package com.unithon.domain.user.application;

import com.unithon.domain.user.dto.UserDTO;


public interface UserService {
    void signUp(String email, String rawPassword);

    UserDTO.LoginResponse login(String email, String rawPassword);

}

