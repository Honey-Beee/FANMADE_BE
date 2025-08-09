package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.dto.AdvertisementDTO;

import java.util.List;

public interface AdvertisementService {
    List<AdvertisementDTO.AdStatusResponse> getFundingAdvertisements();
}
