package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.dto.AdvertisementDTO;

import java.util.List;

public interface AdvertisementService {
    public AdvertisementDTO.MainResponse getAdvertisementsMain(
            String status, String sort, int page, int size);
    AdvertisementDTO.AdvertisementDetailResponse getAdvertisementDetail(Long advertisementId);
    Long createDraft(AdvertisementDTO.CreateDraftRequest req);
    AdvertisementDTO.FundingInfoResponse setFunding(Long adId,
                                                    AdvertisementDTO.FundingInfoRequest req);
    AdvertisementDTO.PlacementListResponse filterPlacements(String mediaType, Integer budget);
    AdvertisementDTO.ChosenPlaceResponse choosePlace(Long adId, AdvertisementDTO.ChoosePlaceRequest req);
    AdvertisementDTO.SummaryResponse getSummary(Long adId);
    AdvertisementDTO.SubmitResponse submitAdvertisement(Long adId);

}
