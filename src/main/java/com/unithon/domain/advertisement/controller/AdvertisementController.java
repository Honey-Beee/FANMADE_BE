package com.unithon.domain.advertisement.controller;

import com.unithon.domain.advertisement.application.AdvertisementService;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.domain.user.dto.UserDTO;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advertisements")
@Slf4j
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    /**
     * 현재 펀딩 중인 모든 광고 리스트 조회 (인증 불필요)
     */
    @GetMapping
    public BaseResponse<AdvertisementDTO.MainResponse> getAdvertisements(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        AdvertisementDTO.MainResponse response =
                advertisementService.getAdvertisementsMain(status, sort, page, size);
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_LIST_SUCCESS, response);
    }

    /**
     * 광고 상세 페이지 정보 조회 (인증 불필요)
     */
    @GetMapping("/{advertisementId}")
    public BaseResponse<AdvertisementDTO.AdvertisementDetailResponse> getAdvertisementDetail(
            @PathVariable Long advertisementId
    ) {
        AdvertisementDTO.AdvertisementDetailResponse response = advertisementService.getAdvertisementDetail(advertisementId);

        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_DETAIL_SUCCESS, response);
    }

    @PostMapping("/drafts")
    public BaseResponse<AdvertisementDTO.CreateDraftResponse> createDraft(
            @RequestBody AdvertisementDTO.CreateDraftRequest request
    ) {
        Long adId = advertisementService.createDraft(request);
        AdvertisementDTO.CreateDraftResponse body =
                AdvertisementDTO.CreateDraftResponse.builder()
                        .adId(adId).status("DRAFT").build();
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_DRAFT_CREATED, body);
    }

    @PatchMapping("/{advertisementId}/funding")
    public BaseResponse<AdvertisementDTO.FundingInfoResponse> setFunding(
            @PathVariable Long advertisementId,
            @RequestBody AdvertisementDTO.FundingInfoRequest request
    ) {
        AdvertisementDTO.FundingInfoResponse res =
                advertisementService.setFunding(advertisementId, request);
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_FUNDING_SAVED, res);
    }

    @GetMapping("/{advertisementId}/places/filter")
    public BaseResponse<AdvertisementDTO.PlacementListResponse> filterPlacements(
            @RequestParam Integer budget
    ) {
        AdvertisementDTO.PlacementListResponse res =
                advertisementService.filterPlacements(budget);
        return BaseResponse.onSuccess(SuccessStatus.PLACEMENT_FILTERED, res);
    }

    @PostMapping("/{advertisementId}/places/choose")
    public BaseResponse<AdvertisementDTO.ChosenPlaceResponse> choosePlace(
            @PathVariable Long advertisementId,
            @RequestBody AdvertisementDTO.ChoosePlaceRequest request
    ) {
        AdvertisementDTO.ChosenPlaceResponse res =
                advertisementService.choosePlace(advertisementId, request);
        return BaseResponse.onSuccess(SuccessStatus.PLACE_CHOSEN, res);
    }

    @GetMapping("/{advertisementId}/summary")
    public BaseResponse<AdvertisementDTO.SummaryResponse> getSummary(
            @PathVariable Long advertisementId
    ) {
        AdvertisementDTO.SummaryResponse res = advertisementService.getSummary(advertisementId);
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_SUMMARY_SUCCESS, res);
    }

    @PostMapping("/{advertisementId}/submit")
    public BaseResponse<AdvertisementDTO.SubmitResponse> submitAdvertisement(
            @PathVariable Long advertisementId
    ) {
        AdvertisementDTO.SubmitResponse res = advertisementService.submitAdvertisement(advertisementId);
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_SUBMITTED, res);
    }

}


