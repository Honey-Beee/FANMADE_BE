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
import com.unithon.domain.bus.domain.entity.Bus;
import com.unithon.domain.subway.domain.entity.Subway;
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

        // --- [추가] 위치 정보 생성 로직 ---
        String location = "";
        Subway subway = ad.getSubway();
        Bus bus = ad.getBus();

        if (subway != null && subway.getSubwayStation() != null) {
            // "신촌역" + " " + "2" + "호선" -> "신촌역 2호선"
            location = subway.getSubwayStation().getName() + " " + subway.getLineCode() + "호선";
        } else if (bus != null) {
            // "N" + " 버스" -> "N 버스"
            location = bus.getBusNumber() + " 버스";
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
                .location(location) // <-- 추가
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
                .mediaType(ad.getMediaType())
                .startDate(ad.getStartDate())
                .endDate(ad.getEndDate())
                .goalAmount(ad.getGoalAmount())
                .build();
    }

    public static AdvertisementDTO.PlacementItem toItem(Subway s) {
        return AdvertisementDTO.PlacementItem.builder()
                .type("SUBWAY")
                .id(s.getId())
                .price(s.getPrice())
                .title(s.getLineCode() + "호선 " + s.getSubwayStation().getName())
                .grade(s.getGrade() != null ? s.getGrade().name() : null)
                .size(new AdvertisementDTO.PlacementItem.Size(s.getSizeWidthCm(), s.getSizeHeightCm()))
                .subwayMeta(
                        AdvertisementDTO.PlacementItem.SubwayMeta.builder()
                                .lineCode(s.getLineCode())
                                .stationName(s.getSubwayStation().getName())
                                .type(s.getType())
                                .build()
                )
                .build();
    }

    public static AdvertisementDTO.PlacementItem toItem(Bus b) {
        return AdvertisementDTO.PlacementItem.builder()
                .type("BUS")
                .id(b.getId())
                .price(b.getPrice())
                .title("버스 " + b.getBusType() + " / " + b.getFace())
                .grade(b.getGrade() != null ? b.getGrade().name() : null)
                .size(new AdvertisementDTO.PlacementItem.Size(b.getSizeWidthCm(), b.getSizeHeightCm()))
                .busMeta(
                        AdvertisementDTO.PlacementItem.BusMeta.builder()
                                .busType(b.getBusType() != null ? b.getBusType().name() : null)
                                .faceType(b.getFace() != null ? b.getFace().name() : null)
                                .build()
                )
                .build();
    }

    public static AdvertisementDTO.ChosenPlaceResponse toChosenFromSubway(Long adId, Subway s) {
        return AdvertisementDTO.ChosenPlaceResponse.builder()
                .adId(adId)
                .mediaType("SUBWAY")
                .placeId(s.getId())
                .build();
    }

    public static AdvertisementDTO.ChosenPlaceResponse toChosenFromBus(Long adId, Bus b) {
        return AdvertisementDTO.ChosenPlaceResponse.builder()
                .adId(adId)
                .mediaType("BUS")
                .placeId(b.getId())
                .build();
    }

    public static AdvertisementDTO.SummaryResponse toSummaryResponse(Advertisement ad, Subway s) {
        return AdvertisementDTO.SummaryResponse.builder()
                .adId(ad.getAdvertisementId())
                .artist(AdvertisementDTO.SummaryResponse.ArtistSummary.builder()
                        .artistId(ad.getArtistId().getId())
                        .artistName(ad.getArtistId().getName())
                        .artistImageUrl(ad.getArtistId().getImageUrl())
                        .build())
                .project(AdvertisementDTO.SummaryResponse.ProjectSummary.builder()
                        .purpose(ad.getPurpose()!=null ? ad.getPurpose().name() : null)
                        .startDate(ad.getStartDate()!=null? ad.getStartDate().toString(): null)
                        .endDate(ad.getEndDate()!=null? ad.getEndDate().toString(): null)
                        .goalAmount(ad.getGoalAmount())
                        .build())
                .mediaType("SUBWAY")
                .subway(AdvertisementDTO.SummaryResponse.SubwaySummary.builder()
                        .grade(s.getGrade()!=null? s.getGrade().name(): null)
                        .type(s.getType()!=null? s.getType().name(): null)
                        .lineCode(s.getLineCode())
                        .stationName(s.getSubwayStation()!=null? s.getSubwayStation().getName(): null)
                        .placement(s.getPlacement())
                        .price(s.getPrice())
                        .sizeWidthCm(s.getSizeWidthCm())
                        .sizeHeightCm(s.getSizeHeightCm())
                        .build())
                .build();
    }

    public static AdvertisementDTO.SummaryResponse toSummaryResponse(Advertisement ad, Bus b) {
        return AdvertisementDTO.SummaryResponse.builder()
                .adId(ad.getAdvertisementId())
                .artist(AdvertisementDTO.SummaryResponse.ArtistSummary.builder()
                        .artistId(ad.getArtistId().getId())
                        .artistName(ad.getArtistId().getName())
                        .artistImageUrl(ad.getArtistId().getImageUrl())
                        .build())
                .project(AdvertisementDTO.SummaryResponse.ProjectSummary.builder()
                        .purpose(ad.getPurpose()!=null ? ad.getPurpose().name() : null)
                        .startDate(ad.getStartDate()!=null? ad.getStartDate().toString(): null)
                        .endDate(ad.getEndDate()!=null? ad.getEndDate().toString(): null)
                        .goalAmount(ad.getGoalAmount())
                        .build())
                .mediaType("BUS")
                .bus(AdvertisementDTO.SummaryResponse.BusSummary.builder()
                        .grade(b.getGrade()!=null? b.getGrade().name(): null)
                        .busType(b.getBusType()!=null? b.getBusType().name(): null)
                        .faceType(b.getFace()!=null? b.getFace().name(): null)
                        .price(b.getPrice())
                        .sizeWidthCm(b.getSizeWidthCm())
                        .sizeHeightCm(b.getSizeHeightCm())
                        .build())
                .build();
    }

}


