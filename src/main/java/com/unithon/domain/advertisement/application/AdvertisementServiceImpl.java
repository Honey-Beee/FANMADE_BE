package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.converter.AdvertisementConverter;
import com.unithon.domain.advertisement.domain.entity.Status;
import com.unithon.domain.advertisement.domain.repository.AdQueryResultInterface;
import com.unithon.domain.advertisement.domain.repository.AdvertisementRepository;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.donation.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService{
    private final AdvertisementRepository advertisementRepository;
    private final DonationRepository donationRepository;
    @Override
    public List<AdvertisementDTO.AdStatusResponse> getMyParticipatedAdvertisements(Long userId) {
        // 1. 사용자가 참여한 광고 ID 목록을 조회합니다.
        List<Long> participatedAdIds = donationRepository.findDistinctAdvertisementIdsByUserId(userId);
        log.info("participatedAdIds :: {}", participatedAdIds);

        // 2. 참여한 광고가 없으면 빈 리스트를 즉시 반환합니다.
        if (participatedAdIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 조회한 ID 목록을 이용해 'FUNDING' 상태인 광고의 상세 정보를 가져옵니다.
        String statusString = Status.FUNDING.name();
        log.info("statusString :: {}", statusString);
        List<AdQueryResultInterface> results = advertisementRepository
                .findAdvertisementsWithDonationCountByIdInAndStatusNative(participatedAdIds, statusString);
        log.info("Service 로직 끝?? -- 서비스단");
        // 4. Converter를 사용해 최종 응답 DTO 리스트로 변환하여 반환합니다.
        return AdvertisementConverter.toAdStatusResponseList(results);

    }
}
