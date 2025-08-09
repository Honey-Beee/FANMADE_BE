package com.unithon.domain.advertisement.domain.repository;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.Status;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    /**
     * [최종 쿼리] 특정 상태(status)의 모든 광고 목록을 상세 정보와 함께 조회
     * @param status 필터링할 광고 상태 (e.g., "FUNDING")
     * @return 광고 상세 정보를 담은 인터페이스 리스트
     */
    @Query(value = "SELECT " +
            "    ad.advertisement_id AS advertisementId, " +
            "    ad.name AS adTitle, " +
            "    ar.name AS artistName, " +
            "    ad.image_url AS imageUrl, " +
            "    ad.media_type AS mediaType, " +
            "    ad.end_date AS endDate, " +
            "    ad.current_amount AS currentAmount, " +
            "    ad.goal_amount AS goalAmount, " +
            "    COUNT(d.donation_id) AS donorCount " +
            "FROM advertisement ad " +
            "LEFT JOIN artist ar ON ad.artist_id = ar.artist_id " +
            "LEFT JOIN donation d ON ad.advertisement_id = d.advertisement_id " +
            "WHERE ad.status = :status " +
            "GROUP BY ad.advertisement_id, ar.name " +
            "ORDER BY ad.end_date ASC", nativeQuery = true)
    List<AdQueryResultInterface> findAllByStatusWithDetails(@Param("status") String status);
}
