package com.unithon.domain.donation.application;

import com.unithon.domain.donation.dto.DonationDTO;

public interface DonationService {
    public DonationDTO.DonationResponse createDonation(
            Long userId, Long advertisementId, DonationDTO.DonationRequest requestDTO);

}
