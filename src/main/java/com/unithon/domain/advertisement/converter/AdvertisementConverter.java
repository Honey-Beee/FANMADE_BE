package com.unithon.domain.advertisement.converter;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.MediaType;
import com.unithon.domain.advertisement.domain.repository.AdQueryResultInterface;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import com.unithon.domain.advertisement.dto.AdvertisementDTO;
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

    public static AdvertisementDTO.AdvertisementDetailResponse toAdvertisementDetailResponse(Advertisement ad, Long donorCount) {

        // --- 계산 로직 수행 ---
        // 1. 남은 기간 계산
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), ad.getEndDate());
        if (remainingDays < 0) {
            remainingDays = 0;
        }

        // 2. 진행률 계산 (목표 금액이 0인 경우 대비)
        int progressPercentage = 0;
        if (ad.getGoalAmount() > 0) {
            progressPercentage = (int) (((double) ad.getCurrentAmount() / ad.getGoalAmount()) * 100);
        }

        // --- DTO 그룹별 빌드 ---
        AdvertisementDTO.AdvertisementInfo adInfo = AdvertisementDTO.AdvertisementInfo.builder()
                .advertisementId(ad.getAdvertisementId())
                .imageUrl(ad.getImageUrl())
                .mediaType(ad.getMediaType())
                .title(ad.getName())
                .artistName(ad.getArtistId().getName())
                .build();

        AdvertisementDTO.FundingStatus fundingStatus = AdvertisementDTO.FundingStatus.builder()
                .currentAmount(ad.getCurrentAmount())
                .goalAmount(ad.getGoalAmount())
                .progressPercentage(progressPercentage)
                .donorCount(donorCount)
                .remainingDays(remainingDays)
                .build();

        AdvertisementDTO.ProjectDetails projectDetails = AdvertisementDTO.ProjectDetails.builder()
                .description(ad.getDescription())
                .build();

        // --- 최종 DTO 조립 및 반환 ---
        return AdvertisementDTO.AdvertisementDetailResponse.builder()
                .advertisementInfo(adInfo)
                .fundingStatus(fundingStatus)
                .projectDetails(projectDetails)
                .build();
    }

}


