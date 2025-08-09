package com.unithon.domain.donation.application;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.repository.AdvertisementRepository;
import com.unithon.domain.donation.converter.DonationConverter;
import com.unithon.domain.donation.domain.Donation;
import com.unithon.domain.donation.dto.DonationDTO;
import com.unithon.domain.donation.repository.DonationRepository;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final DonationRepository donationRepository;

    @Override
    public DonationDTO.DonationResponse createDonation(Long userId, Long advertisementId, DonationDTO.DonationRequest requestDTO) {
        // 1. 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("광고를 찾을 수 없습니다. ID: " + advertisementId));

        // 2. Donation 엔티티 생성 및 저장 (INSERT)
        Donation newDonation = Donation.builder()
                .user(user)
                .advertisement(advertisement)
                .amount(requestDTO.getAmount())
                .message(requestDTO.getMessage())
                .build();
        donationRepository.save(newDonation);

        // 3. Advertisement 엔티티의 현재 모금액 업데이트 (UPDATE)
        advertisement.addCurrentAmount(requestDTO.getAmount());
        advertisementRepository.save(advertisement);

        // 5. 최종 응답 DTO 생성 및 반환
        return DonationConverter.toDonationResponse(newDonation);
    }

}
