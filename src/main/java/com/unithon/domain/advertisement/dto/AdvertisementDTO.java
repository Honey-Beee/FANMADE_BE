package com.unithon.domain.advertisement.dto;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.MediaType;
import lombok.*;

public class AdvertisementDTO {

    /**
     * 광고 현황 리스트 각 아이템에 대한 최종 응답 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AdStatusResponse {
        private Long advertisementId;
        private String adTitle; // BTS 지하철 광고
        private String artistName; // BTS
        private String imageUrl; // 섬네일 이미지
        private MediaType mediaType; // SUBWAY, BUS
        private int currentAmount; // 현재 금액 수
        private int goalAmount; // 목표 금액 수
        private Long donorCount; // 후원자 수
        private Long remainingDays; // 남은 일수
    }

    /**
     * Repository에서 JPQL 결과를 받아오기 위한 중간 DTO
     */
    @Getter
    @AllArgsConstructor
    public static class AdQueryResult {
        private Advertisement advertisement;
        private Long donorCount;
    }
}