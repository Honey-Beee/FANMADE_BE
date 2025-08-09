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
        log.info("Controller log, response :: {}", response);
        return BaseResponse.onSuccess(SuccessStatus.ADVERTISEMENT_DETAIL_SUCCESS, response);
    }


}


