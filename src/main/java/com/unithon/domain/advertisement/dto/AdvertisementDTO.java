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
     * [최종 응답] 상세 페이지 전체를 나타내는 DTO
     */
    @Builder
    @Getter
    public static class AdvertisementDetailResponse {
        private AdvertisementInfo advertisementInfo;
        private ArtistInfo artistInfo;
        private ProjectInfo projectInfo;
        private FundingStatus fundingStatus;
    }

    /**
     * 상단 커버 이미지와 태그 정보를 담는 DTO
     */
    @Builder
    @Getter
    public static class AdvertisementInfo {
        private String coverImageUrl;
        private Purpose purpose;
        private String status; // "진행중", "완료" 등 한글 문자열을 담을 필드
    }

    /**
     * 아티스트 정보를 담는 DTO
     */
    @Builder
    @Getter
    public static class ArtistInfo {
        private Long artistId;
        private String name;
        private String profileImageUrl;
    }

    /**
     * 프로젝트 제목, 설명, 기간 등 텍스트 정보를 담는 DTO
     */
    @Builder
    @Getter
    public static class ProjectInfo {
        private String title;
        private String description;
        private String startDate;
        private String endDate;
    }

    /**
     * 모금 현황 숫자 정보를 담는 DTO
     */
    @Builder
    @Getter
    public static class FundingStatus {
        private int currentAmount; // 모금된 금액
        private int goalAmount; // 목표 금액
        private Long donorCount; // 후원자 수
        private Long remainingDays; // 남은 기간
        private int progressPercentage; // 달성률
    }

}