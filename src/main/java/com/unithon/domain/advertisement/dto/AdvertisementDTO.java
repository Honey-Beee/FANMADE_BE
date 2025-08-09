package com.unithon.domain.advertisement.dto;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.MediaType;
import com.unithon.domain.advertisement.domain.entity.Purpose;
import lombok.*;

import java.util.List;

public class AdvertisementDTO {

    /**
     * 광고 메인 페이지 DTO
     */
    @Builder
    @Getter
    public static class MainResponse {
        private SummaryInfo summaryInfo;
        private List<AdvertisementCard> advertisements;
        private Pagination pagination; // 페이지네이션 정보 추가
    }

    /**
     * 상단 요약 정보 카드 DTO
     */
    @Builder
    @Getter
    public static class SummaryInfo {
        private long fundingProjectCount; // 진행 중인 프로젝트 수
        private long totalDonorCount;     // 총 참여자 수
        private long totalFundingAmount;  // 총 모금액
    }

    /**
     * 광고 카드 하나를 나타내는 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdvertisementCard {
        private Long advertisementId;
        private Purpose purpose;          // 생일 광고, 기타 응원 등
        private String artistName;
        private String title;
        private int progressPercentage;   // 진행률
        private int currentAmount;        // 현재 모금액
        private Long donorCount;          // 참여자 수
        private Long remainingDays;       // 남은 기간
      //  private String location;          // 광고 장소 (e.g., 타임스퀘어 전광판)
        private String imageUrl;
    }

    @Builder
    @Getter
    public static class Pagination {
        private int currentPage;
        private int totalPages;
        private long totalElements;
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