package com.unithon.domain.advertisement.application;

import com.unithon.domain.advertisement.converter.AdvertisementConverter;
import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.Status;
import com.unithon.domain.advertisement.domain.repository.AdQueryResultInterface;
import com.unithon.domain.advertisement.domain.repository.AdvertisementRepository;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.donation.repository.DonationRepository;
import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.ArtistRepository;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.global.error.code.status.ErrorStatus;
import com.unithon.global.exception.GeneralException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService{
    private final AdvertisementRepository advertisementRepository;
    private final EntityManager em;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    @Override
    @Transactional(readOnly = true)
    public AdvertisementDTO.MainResponse getAdvertisementsMain(String statusStr, String sortStr, int page, int size) {
        em.clear();

        // --- 1. 요약 정보 조회 ---
        Object[] summaryData;
        if (statusStr == null || statusStr.isBlank()) { // 전체
            summaryData = advertisementRepository.findSummaryInfoAll().get(0);
            log.info("전체 광고 리스트에 걸렸습니다. summaryData [0] [1] [2]:: {}, {}", summaryData, summaryData[0]);
        } else { // 상태별로
            summaryData = advertisementRepository.findSummaryInfoByStatus(Status.valueOf(statusStr)).get(0);
            log.info("상태 광고 리스트에 걸렸습니다 . summaryData [0] [1] [2]:: {}, {}", summaryData, summaryData[0]);
        }

        AdvertisementDTO.SummaryInfo summaryInfo = AdvertisementDTO.SummaryInfo.builder()
                .fundingProjectCount(summaryData[0] != null ? (Long) summaryData[0] : 0L)
                .totalDonorCount(summaryData[1] != null ? (Long) summaryData[1] : 0L)
                .totalFundingAmount(summaryData[2] != null ? (Long) summaryData[2] : 0L)
                .build();

        // --- 2. 정렬 조건 생성 ---
        Sort sort = switch (sortStr) {
            case "closingSoon" -> Sort.by(Sort.Direction.ASC, "endDate"); // 마감임박순
            case "highestAmount" -> Sort.by(Sort.Direction.DESC, "currentAmount"); // 모금액순
            // 'popular'를 포함한 나머지는 모두 최신순으로 DB에서 가져옵니다.
            default -> Sort.by(Sort.Direction.DESC, "createdAt"); // 최신순
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        // --- 3. 필터링 조건에 따라 광고 목록(Page) 조회 (1차 쿼리) ---
        Page<Advertisement> adPage;
        if (statusStr == null || statusStr.isBlank()) { // 전체
            adPage = advertisementRepository.findAll(pageable);
        } else { // 진행중/완료
            adPage = advertisementRepository.findByStatus(Status.valueOf(statusStr), pageable);
        }

        if (!adPage.hasContent()) {
            // 조회된 광고가 없으면 빈 응답 반환
            return AdvertisementConverter.toMainResponse(summaryInfo, Collections.emptyList(), adPage);
        }

        // --- 4. 후원자 수 조회를 위한 ID 목록 추출 및 2차 쿼리 실행 ---
        List<Long> adIds = adPage.getContent().stream()
                .map(Advertisement::getAdvertisementId)
                .collect(Collectors.toList());

        // [광고 ID, 후원자 수] Map 생성 (조회를 쉽게 하기 위함)
        Map<Long, Long> donorCountsMap = advertisementRepository.findDonorCountsByAdvertisementIds(adIds).stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        // --- 5. 최종 DTO 리스트 생성 ---
        List<AdvertisementDTO.AdvertisementCard> adCards = adPage.getContent().stream()
                .map(ad -> {
                    // Map에서 해당 광고의 후원자 수를 찾음. 없으면 0으로 처리.
                    Long donorCount = donorCountsMap.getOrDefault(ad.getAdvertisementId(), 0L);
                    // Converter에 엔티티와 후원자 수를 함께 넘겨줌
                    return AdvertisementConverter.toAdvertisementCard(ad, donorCount);
                })
                .collect(Collectors.toList());

        if ("popular".equals(sortStr)) {
            adCards.sort(Comparator.comparing(AdvertisementDTO.AdvertisementCard::getDonorCount).reversed());
        }

        // --- 6. 최종 응답 DTO 조립 및 반환 ---
        return AdvertisementConverter.toMainResponse(summaryInfo, adCards, adPage);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvertisementDTO.AdvertisementDetailResponse getAdvertisementDetail(Long advertisementId) {
        em.clear();

        // 1. Repository를 호출하여 [Advertisement, donorCount] 형태의 결과를 조회
        Object[] result = advertisementRepository.findAdvertisementWithDonorCountById(advertisementId)
                .orElseThrow(() -> new RuntimeException("해당 광고를 찾을 수 없습니다. ID: " + advertisementId));

        log.info("광고 디테일 페이지 result : {}, result[0] : {}", result, result[0]);

        Object[] innerResult = (Object[]) result[0];

        Advertisement advertisement = (Advertisement) innerResult[0]; // 내부 배열의 첫 번째 요소
        Long donorCount = (Long) innerResult[1];           // 내부 배열의 두 번째 요소

        // 3. Converter를 사용하여 최종 DTO로 변환 후 반환
        return AdvertisementConverter.toAdvertisementDetailResponse(advertisement, donorCount);
    }

    @Override
    @Transactional
    public Long createDraft(AdvertisementDTO.CreateDraftRequest req) {
        Long userId = currentUserId();
        User user = userRepository.getReferenceById(userId);
        Artist artist = artistRepository.getReferenceById(req.getArtistId());

        Advertisement ad = AdvertisementConverter.toDraftEntity(artist, user, req);

        return advertisementRepository.save(ad).getAdvertisementId();
    }

    @Override
    @Transactional
    public AdvertisementDTO.FundingInfoResponse setFunding(Long adId
                                                           ,AdvertisementDTO.FundingInfoRequest req) {
        Long userId = currentUserId();
        Advertisement ad = advertisementRepository
                .findByAdvertisementIdAndUser_Id(adId, userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.AD_NOT_FOUND));

        if (req.getGoalAmount() == null || req.getGoalAmount() < 100_000) {
            throw new GeneralException(ErrorStatus.INVALID_FUNDING_GOAL);
        }
        if (req.getStartDate() == null || req.getEndDate() == null ||
                !req.getEndDate().isAfter(req.getStartDate())) {
            throw new GeneralException(ErrorStatus.INVALID_FUNDING_PERIOD);
        }

        ad.applyFunding(req.getStartDate(), req.getEndDate(), req.getGoalAmount());

        return AdvertisementConverter.toFundingInfoResponse(ad);
    }

    public Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new RuntimeException("인증 필요");

        Object principal = auth.getPrincipal();
        String email = (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : String.valueOf(principal);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음: " + email));
        return user.getId();
    }

}
