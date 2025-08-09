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
     * 상세 페이지를 위한 DTO 구조
     */

    @Builder
    @Getter
    public static class AdvertisementDetailResponse {
        private AdvertisementInfo advertisementInfo;
        private FundingStatus fundingStatus;
        private ProjectDetails projectDetails;
    }

    @Builder
    @Getter
    public static class AdvertisementInfo {
        private Long advertisementId;
        private String imageUrl;
        private MediaType mediaType;
        private String title;
        private String artistName;
    }

    @Builder
    @Getter
    public static class FundingStatus {
        private int currentAmount;
        private int goalAmount;
        private int progressPercentage; // 진행률 (계산된 값)
        private Long donorCount;
        private Long remainingDays; // 남은 기간 (계산된 값)
    }

    @Builder
    @Getter
    public static class ProjectDetails {
        private String description;
    }

}