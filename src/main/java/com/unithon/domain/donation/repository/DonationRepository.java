package com.unithon.domain.donation.repository;

import com.unithon.domain.donation.domain.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    /**
     * 특정 사용자가 '후원한' 광고의 ID 목록을 중복 없이 조회하는 메서드
     * @param userId 후원자의 ID
     * @return 광고 ID 리스트
     */
    @Query("SELECT DISTINCT d.advertisement.advertisementId FROM Donation d WHERE d.user.id = :userId")
    List<Long> findDistinctAdvertisementIdsByDonorId(@Param("userId") Long userId);

    /**
     * 특정 광고에 대해, 유저별 총 후원 금액을 계산하고,
     * 후원 금액이 높은 순으로 정렬하여 상위 N개의 결과를 Page 객체로 반환합니다.
     * @param advertisementId 조회할 광고 ID
     * @param pageable 페이지 정보 (LIMIT 역할 수행, e.g., PageRequest.of(0, 3))
     * @return [User 엔티티, 총 후원 금액] 형태의 Object 배열을 담은 Page
     */
    @Query("SELECT d.user, SUM(d.amount) AS totalAmount " +
            "FROM Donation d " +
            "WHERE d.advertisement.advertisementId = :advertisementId " +
            "GROUP BY d.user " +
            "ORDER BY totalAmount DESC")
    Page<Object[]> findTopDonorsByAdvertisementId(
            @Param("advertisementId") Long advertisementId, Pageable pageable);
}
