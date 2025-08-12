package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.dto.AdvertisementDTO;

import java.util.List;

public interface AdvertisementService {
    public AdvertisementDTO.MainResponse getAdvertisementsMain(
            String status, String sort, int page, int size);
    AdvertisementDTO.AdvertisementDetailResponse getAdvertisementDetail(Long advertisementId);
    AdvertisementDTO.PlacementListResponse filterPlacements(AdvertisementDTO.filterRequest request);
    AdvertisementDTO.CreateAdResponse submitAdvertisement(AdvertisementDTO.CreateAdRequest request);

}
