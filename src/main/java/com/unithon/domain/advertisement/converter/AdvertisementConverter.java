package com.unithon.domain.advertisement.converter;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.MediaType;
import com.unithon.domain.advertisement.domain.entity.Status;
import com.unithon.domain.advertisement.domain.repository.AdQueryResultInterface;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class AdvertisementConverter {

    // 광고현황 - 메인 DTO

    public static AdvertisementDTO.MainResponse toMainResponse(
            AdvertisementDTO.SummaryInfo summaryInfo,
            List<AdvertisementDTO.AdvertisementCard> adCards,
            Page<Advertisement> adPage) {

        AdvertisementDTO.Pagination pagination = AdvertisementDTO.Pagination.builder()
                .currentPage(adPage.getNumber())
                .totalPages(adPage.getTotalPages())
                .totalElements(adPage.getTotalElements())
                .build();

        return AdvertisementDTO.MainResponse.builder()
                .summaryInfo(summaryInfo)
                .advertisements(adCards)
                .pagination(pagination)
                .build();
    }

    /**
     * Advertisement 엔티티와 후원자 수를 받아 광고 카드 DTO로 변환합니다.
     * @param ad Advertisement 엔티티
     * @param donorCount 해당 광고의 후원자 수
     * @return AdvertisementCard DTO
     */
    public static AdvertisementDTO.AdvertisementCard toAdvertisementCard(Advertisement ad, Long donorCount) {
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), ad.getEndDate());
        if (remainingDays < 0) remainingDays = 0;

        int progressPercentage = 0;
        if (ad.getGoalAmount() > 0) {
            progressPercentage = (int) (((double) ad.getCurrentAmount() / ad.getGoalAmount()) * 100);
        }

        return AdvertisementDTO.AdvertisementCard.builder()
                .advertisementId(ad.getAdvertisementId())
                .purpose(ad.getPurpose())
                .artistName(ad.getArtistId().getName())
                .title(ad.getName())
                .progressPercentage(progressPercentage)
                .currentAmount(ad.getCurrentAmount())
                .donorCount(donorCount) // 계산된 값을 직접 주입
                .remainingDays(remainingDays)
                .imageUrl(ad.getImageUrl())
                .build();
    }

    // 광고 상세 페이지 컨버터
    public static AdvertisementDTO.AdvertisementDetailResponse toAdvertisementDetailResponse(Advertisement ad, Long donorCount) {

        // --- 데이터 가공 로직 ---
        // 1. 상태(Status) Enum을 한글 문자열로 변환
        String statusInKorean = switch (ad.getStatus()) {
            case FUNDING -> "진행중";
            case FUNDED -> "완료";
            case CANCELED -> "취소";
            default -> "기타";
        };

        // 2. 남은 기간 계산
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), ad.getEndDate());
        if (remainingDays < 0) remainingDays = 0;

        // 3. 진행률 계산 (목표 금액이 0인 경우 대비)
        int progressPercentage = (ad.getGoalAmount() > 0) ?
                (int) (((double) ad.getCurrentAmount() / ad.getGoalAmount()) * 100) : 0;

        // --- 그룹별 DTO 빌드 ---
        AdvertisementDTO.AdvertisementInfo adInfo = AdvertisementDTO.AdvertisementInfo.builder()
                .coverImageUrl(ad.getImageUrl()) // 엔티티의 imageUrl을 커버이미지로 사용
                .purpose(ad.getPurpose())
                .status(statusInKorean)
                .build();

        AdvertisementDTO.ArtistInfo artistInfo = AdvertisementDTO.ArtistInfo.builder()
                .artistId(ad.getArtistId().getId())
                .name(ad.getArtistId().getName())
                .profileImageUrl(ad.getArtistId().getImageUrl()) // Artist 엔티티에 imageUrl 필드가 있다고 가정
                .build();

        AdvertisementDTO.ProjectInfo projectInfo = AdvertisementDTO.ProjectInfo.builder()
                .title(ad.getName())
                .description(ad.getDescription())
                .startDate(ad.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE)) // YYYY-MM-DD 형식
                .endDate(ad.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .build();

        AdvertisementDTO.FundingStatus fundingStatus = AdvertisementDTO.FundingStatus.builder()
                .currentAmount(ad.getCurrentAmount())
                .goalAmount(ad.getGoalAmount())
                .donorCount(donorCount)
                .remainingDays(remainingDays)
                .progressPercentage(progressPercentage)
                .build();

        // --- 최종 응답 DTO 조립 ---
        return AdvertisementDTO.AdvertisementDetailResponse.builder()
                .advertisementInfo(adInfo)
                .artistInfo(artistInfo)
                .projectInfo(projectInfo)
                .fundingStatus(fundingStatus)
                .build();
    }

    public static Advertisement toDraftEntity(Artist artist, User user, AdvertisementDTO.CreateDraftRequest req) {
        return Advertisement.builder()
                .artistId(artist)
                .user(user)
                .name(req.getName())
                .description(req.getDescription())
                .status(Status.DRAFT)   // 드래프트 상태
                .purpose(req.getPurpose())
                .currentAmount(0)
                .goalAmount(0)
                .build();
    }

    public static AdvertisementDTO.FundingInfoResponse toFundingInfoResponse(Advertisement ad) {
        return AdvertisementDTO.FundingInfoResponse.builder()
                .adId(ad.getAdvertisementId())
                .startDate(ad.getStartDate())
                .endDate(ad.getEndDate())
                .goalAmount(ad.getGoalAmount())
                .build();
    }


}


