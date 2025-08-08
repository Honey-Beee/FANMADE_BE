package com.unithon.user.service;

import com.unithon.config.JwtUtil;
import com.unithon.user.domain.entity.User;
import com.unithon.user.domain.repository.UserRepository;
import com.unithon.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signUp(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.updateEmail(email);
        user.updatePassword(hashedPassword);
        userRepository.save(user);
    }

    public UserDTO.LoginResponse login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String jwt = jwtUtil.generateToken(user);
        return new UserDTO.LoginResponse(user.getId(), jwt);
    }
}
