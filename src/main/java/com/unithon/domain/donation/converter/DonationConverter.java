package com.unithon.domain.donation.converter;

import com.unithon.domain.donation.domain.Donation;
import com.unithon.domain.donation.dto.DonationDTO;

public class DonationConverter {
    /**
     * 생성된 Donation 엔티티를 DonationResponse DTO로 변환합니다.
     * @param donation 방금 저장된 Donation 객체
     * @return 생성된 donation의 ID를 담은 DTO
     */
    public static DonationDTO.DonationResponse toDonationResponse(Donation donation) {
        return DonationDTO.DonationResponse.builder()
                .createdDonationId(donation.getId())
                .build();
    }
}
