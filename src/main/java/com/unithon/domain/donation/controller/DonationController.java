package com.unithon.domain.donation.controller;

import com.unithon.domain.donation.application.DonationService;
import com.unithon.domain.donation.dto.DonationDTO;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.domain.user.dto.UserDTO;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advertisements/{advertisementId}/donations")
public class DonationController {
    private final DonationService donationService;
    private final UserRepository userRepository;

    /**
     * 특정 광고에 후원하기 (인증 필요)
     */
    @PostMapping
    public BaseResponse<DonationDTO.DonationResponse> createDonation(
            @PathVariable Long advertisementId,
            @RequestBody DonationDTO.DonationRequest requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 1. @AuthenticationPrincipal에서 얻은 정보(email)로 User 엔티티를 조회
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("인증된 사용자를 찾을 수 없습니다."));

        // 2. 서비스 로직 호출
        DonationDTO.DonationResponse response =
                donationService.createDonation(user.getId(), advertisementId, requestDTO);

        // 3. 성공 응답 반환
        return BaseResponse.onSuccess(SuccessStatus.DONATION_SUCCESS, response);
    }

    /**
     * 특정 광고의 Top 3 후원자 랭킹 조회 (인증 불필요)
     */
    @GetMapping("/top-donors")
    public BaseResponse<List<DonationDTO.TopDonorResponse>> getTop3Donors(
            @PathVariable Long advertisementId
    ) {
        List<DonationDTO.TopDonorResponse> response = donationService.getTop3Donors(advertisementId);
        return BaseResponse.onSuccess(SuccessStatus.TOP_DONOR_LIST_SUCCESS, response);
    }



}

