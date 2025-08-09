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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class AdvertisementConverter {

    /**
     * [수정됨] AdQueryResultInterface 객체 하나를 AdStatusResponse DTO로 변환하는 메서드
     * @param result Native Query의 조회 결과 한 줄 (인터페이스 타입)
     * @return 화면에 표시될 최종 DTO
     */
    public static AdvertisementDTO.AdStatusResponse toAdStatusResponse(AdQueryResultInterface result) {
        // 남은 일수 계산
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), result.getEndDate());
        if (remainingDays < 0) {
            remainingDays = 0; // 마감일이 지났으면 0으로 표시
        }

        // Native Query 결과(String)를 MediaType(Enum)으로 변환
        MediaType mediaType = MediaType.valueOf(result.getMediaType());

        return AdvertisementDTO.AdStatusResponse.builder()
                .advertisementId(result.getAdvertisementId())
                .adTitle(result.getAdTitle())
                .artistName(result.getArtistName())
                .imageUrl(result.getImageUrl())
                .mediaType(mediaType) // 변환된 Enum 값을 사용
                .currentAmount(result.getCurrentAmount())
                .goalAmount(result.getGoalAmount())
                .donorCount(result.getDonorCount())
                .remainingDays(remainingDays)
                .build();
    }

    /**
     * [수정됨] AdQueryResultInterface 리스트 전체를 AdStatusResponse DTO 리스트로 변환하는 메서드
     * @param results Native Query의 전체 조회 결과 (인터페이스 리스트)
     * @return 화면에 표시될 최종 DTO 리스트
     */
    public static List<AdvertisementDTO.AdStatusResponse> toAdStatusResponseList(List<AdQueryResultInterface> results) {
        return results.stream()
                .map(AdvertisementConverter::toAdStatusResponse) // 각 항목을 위에서 정의한 메서드로 변환
                .collect(Collectors.toList());
    }
}

//public class AdvertisementConverter {
//
//    public static AdvertisementDTO.AdStatusResponse toAdStatusResponse(Advertisement advertisement, Long donorCount) {
//        // 남은 일수 계산
//        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), advertisement.getEndDate());
//        if (remainingDays < 0) {
//            remainingDays = 0; // 마감일이 지났으면 0으로 표시
//        }
//
//        return AdvertisementDTO.AdStatusResponse.builder()
//                .advertisementId(advertisement.getAdvertisementId())
//                .adTitle(advertisement.getName())
//                .artistName(advertisement.getArtistId().getName()) // 연관된 Artist 엔티티에서 이름 가져오기
//                .imageUrl(advertisement.getImageUrl())
//                .mediaType(advertisement.getMediaType())
//                .currentAmount(advertisement.getCurrentAmount())
//                .goalAmount(advertisement.getGoalAmount())
//                .donorCount(donorCount)
//                .remainingDays(remainingDays)
//                .build();
//    }
//
//    public static List<AdvertisementDTO.AdStatusResponse> toAdStatusResponseList(List<AdvertisementDTO.AdQueryResult> results) {
//        return results.stream()
//                .map(result -> toAdStatusResponse(result.getAdvertisement(), result.getDonorCount()))
//                .collect(Collectors.toList());
//    }
//}
