package com.unithon.domain.donation.application;

import com.unithon.domain.donation.dto.DonationDTO;

import java.util.List;

public interface DonationService {
    public DonationDTO.DonationResponse createDonation(
            Long userId, Long advertisementId, DonationDTO.DonationRequest requestDTO);


    public List<DonationDTO.TopDonorResponse> getTop3Donors(Long advertisementId);

}
