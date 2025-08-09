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

    public static AdvertisementDTO.AdStatusResponse convertToAdStatusDTO(AdQueryResultInterface result) {
        // 남은 일수 계산
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), result.getEndDate());
        if (remainingDays < 0) {
            remainingDays = 0;
        }

        return AdvertisementDTO.AdStatusResponse.builder()
                .advertisementId(result.getAdvertisementId())
                .adTitle(result.getAdTitle())
                .artistName(result.getArtistName())
                .imageUrl(result.getImageUrl())
                .mediaType(MediaType.valueOf(result.getMediaType())) // String -> Enum 변환
                .currentAmount(result.getCurrentAmount())
                .goalAmount(result.getGoalAmount())
                .donorCount(result.getDonorCount())
                .remainingDays(remainingDays)
                .build();
    }

}


