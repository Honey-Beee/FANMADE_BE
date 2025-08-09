package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.converter.AdvertisementConverter;
import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.Status;
import com.unithon.domain.advertisement.domain.repository.AdQueryResultInterface;
import com.unithon.domain.advertisement.domain.repository.AdvertisementRepository;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.donation.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService{
    private final AdvertisementRepository advertisementRepository;

    @Override
    public List<AdvertisementDTO.AdStatusResponse> getFundingAdvertisements() {
        List<AdQueryResultInterface> results = advertisementRepository.findAllByStatusWithDetails("FUNDING");

        return results.stream()
                .map(AdvertisementConverter::convertToAdStatusDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdvertisementDTO.AdvertisementDetailResponse getAdvertisementDetail(Long advertisementId) {
        // 광고 정보와 후원자 수를 조회
        Object[] result = advertisementRepository.findAdvertisementWithDonorCountById(advertisementId)
                .orElseThrow(() -> new RuntimeException("해당 광고를 찾을 수 없습니다. ID: " + advertisementId));

        log.info("Service log, result :: {} {}", result, result[0]);

        Object[] innerResult = (Object[]) result[0];
        log.info("Service log, innerResult :: {} {}", innerResult);
        Advertisement advertisement = (Advertisement) innerResult[0];
        Long donorCount = (Long) innerResult[1];

        log.info("Service log, 광고명, 도네카운트 :: {} {}", advertisement.getName(), donorCount);

        return AdvertisementConverter.toAdvertisementDetailResponse(advertisement, donorCount);

    }
}
