package com.unithon.user.application;

import com.unithon.config.JwtUtil;
import com.unithon.user.domain.entity.User;
import com.unithon.user.domain.repository.UserRepository;
import com.unithon.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {
    void signUp(String email, String rawPassword);

    UserDTO.LoginResponse login(String email, String rawPassword);

}

