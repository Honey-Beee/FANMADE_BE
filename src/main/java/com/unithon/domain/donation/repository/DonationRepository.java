package com.unithon.domain.donation.repository;

import com.unithon.domain.donation.domain.Donation;
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
}
