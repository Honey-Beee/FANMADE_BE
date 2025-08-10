package com.unithon.domain.advertisement.dto;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.MediaType;
import com.unithon.domain.advertisement.domain.entity.Purpose;
import com.unithon.domain.subway.domain.entity.Type;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        private String imageUrl;
        private String location;          // 버스 -> ex) N버스, (버스번호) 버스, 지하철 -> xx역 xx선
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
        private String location; // 추가
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDraftRequest {
        private Long artistId;
        private Purpose purpose;     // BIRTHDAY/DEBUT/COMEBACK/ETC
        private String name;         // 프로젝트명(카드 타이틀)
        private String description;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDraftResponse {
        private Long adId;
        private String status; // "DRAFT"
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FundingInfoRequest {
        private LocalDate startDate;
        private LocalDate endDate;
        private MediaType mediaType;
        private Integer goalAmount;  // 최소 100000 권장
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FundingInfoResponse {
        private Long adId;
        private LocalDate startDate;
        private LocalDate endDate;
        private MediaType mediaType;
        private Integer goalAmount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacementItem {
        private String type;        // SUBWAY | BUS
        private Long id;
        private Integer price;
        private String title;       // 화면 표시용
        private Size size;          // 공통: 규격

        // 공통 등급
        private String grade;       // SSA/SA/A

        // 매체별 상세정보
        private SubwayMeta subwayMeta;
        private BusMeta busMeta;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Size {
            private Integer w; // cm
            private Integer h; // cm
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SubwayMeta {
            private int lineCode;   // 노선
            private String stationName;
            private Type type;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BusMeta {
            private String busType;    // 일반, 광역, ...
            private String faceType;   // 전면, 측면, 후면 등
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacementListResponse {
        private String mediaType;
        private Integer budget;
        private List<PlacementItem> items;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChoosePlaceRequest {
        private MediaType mediaType;   // "SUBWAY" | "BUS"
        private Long placeId;  // Subway.id or Bus.id
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChosenPlaceResponse {
        private Long adId;
        private String mediaType;   // "SUBWAY" | "BUS"
        private Long placeId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryResponse {
        private Long adId;

        private ArtistSummary artist;     // 아티스트 요약
        private ProjectSummary project;   // 목적/기간/목표금액/실제사용예산
        private String mediaType;         // "SUBWAY" | "BUS"

        // 매체별 상세 (둘 중 하나만 채움)
        private SubwaySummary subway;
        private BusSummary bus;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ArtistSummary {
            private Long artistId;
            private String artistName;
            private String artistImageUrl;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ProjectSummary {
            private String purpose;       // enum name or 한글 표기
            private String startDate;     // YYYY-MM-DD
            private String endDate;       // YYYY-MM-DD
            private Integer goalAmount;   // 목표금액
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SubwaySummary {
            private String grade;       // SSA/SA/A
            private String type;        // A/B
            private Integer lineCode;   // 호선
            private String stationName; // 역명
            private String placement;   // 배치(승강장 전광판 등)
            private Integer price;
            private Integer sizeWidthCm;
            private Integer sizeHeightCm;
        }

        @Getter @Builder @NoArgsConstructor @AllArgsConstructor
        public static class BusSummary {
            private String grade;       // SSA/SA/A
            private String busType;     // A형/B형/...
            private String faceType;    // 차도면/인도면/후면/...
            private Integer price;
            private Integer sizeWidthCm;
            private Integer sizeHeightCm;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitResponse {
        private Long adId;
        private String status;
    }


}