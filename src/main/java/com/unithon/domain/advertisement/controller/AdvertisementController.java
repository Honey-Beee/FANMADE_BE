package com.unithon.domain.advertisement.controller;

import com.unithon.domain.advertisement.application.AdvertisementService;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.domain.user.dto.UserDTO;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advertisements")
public class AdvertisementController {
    private final AdvertisementService advertisementService;
    private final UserRepository userRepository;

    /**
     * "로그인한 사용자가" "참여한/후원한" 광고 현황 조회 api
     */
    @GetMapping("/me/participated")
    public BaseResponse<List<AdvertisementDTO.AdStatusResponse>> getMyParticipatedAdvertisements(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. UserDetails에서 이메일(또는 식별자)을 가져와 DB에서 User 엔티티를 조회합니다.
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")); // 실제로는 적절한 예외 처리 필요

        // 2. 조회한 사용자 ID를 서비스에 전달하여 비즈니스 로직을 수행합니다.
        List<AdvertisementDTO.AdStatusResponse> response = advertisementService.getMyParticipatedAdvertisements(user.getId());

        // 3. 성공 응답을 반환합니다.
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_MY_LIST_SUCCESS, response);
    }
}


