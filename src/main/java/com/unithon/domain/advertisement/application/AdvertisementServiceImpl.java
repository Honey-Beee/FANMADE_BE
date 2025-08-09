package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.converter.AdvertisementConverter;
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
}
