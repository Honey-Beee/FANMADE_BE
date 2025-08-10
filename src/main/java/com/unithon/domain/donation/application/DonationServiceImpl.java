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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public List<DonationDTO.TopDonorResponse> getTop3Donors(Long advertisementId) {
        // 1. "전체 결과 중 상위 3개"를 의미하는 Pageable 객체 생성
        PageRequest pageable = PageRequest.of(0, 3);

        // 2. Repository를 호출하여 정렬 및 그룹화가 완료된 상위 3개의 데이터를 조회
        Page<Object[]> topDonorsPage =
                donationRepository.findTopDonorsByAdvertisementId(advertisementId, pageable);
        log.info("topDonerService :: toDonorsPage :{}, list :: {}" , topDonorsPage, topDonorsPage.get().collect(Collectors.toList()));

        List<Object[]> topDonors = topDonorsPage.getContent();

        // 3. 조회된 결과 리스트에 1, 2, 3 순위를 부여하면서 DTO로 변환
        return IntStream.range(0, topDonors.size())
                .mapToObj(index -> {
                    Object[] row = topDonors.get(index);
                    User user = (User) row[0];
                    Long totalAmount = (Long) row[1];
                    // Converter를 사용하여 DTO 생성 (순위는 1부터 시작)
                    return DonationConverter.toTopDonorResponse(index + 1, user, totalAmount);
                })
                .collect(Collectors.toList());
    }

}
