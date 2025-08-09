package com.unithon.domain.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DonationDTO {

    /**
     * 후원하기 요청(Request)을 위한 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonationRequest {
        private Long amount; // 후원금액
        private String message; // 응원 메시지 (선택 사항)
    }

    /**
     * 후원 성공 후 응답(Response)을 위한 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DonationResponse {
        private Long createdDonationId;
    }
}


