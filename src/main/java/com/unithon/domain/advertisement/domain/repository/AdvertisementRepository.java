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
     * 주어진 광고 ID 목록에 해당하는 광고들의 상세 정보(후원자 수 포함)를 조회합니다.
     * 'FUNDING' 상태인 광고만 필터링합니다.
     * @param adIds 조회할 광고 ID 리스트
     * @param status 필터링할 광고 상태 (e.g., Status.FUNDING)
     * @return 광고 정보와 후원자 수를 담은 DTO 리스트
     */
//    @Query("SELECT new com.unithon.domain.advertisement.dto.AdvertisementDTO.AdQueryResult(ad, COUNT(d.id)) " +
//            "FROM Advertisement ad " +
//            "LEFT JOIN ad.artistId ar " + // N+1 문제 방지를 위한 fetch join
//            "LEFT JOIN Donation d ON d.advertisement = ad " +
//            "WHERE ad.advertisementId IN :adIds AND ad.status = :status " +
//            "GROUP BY ad.advertisementId")
//    List<AdvertisementDTO.AdQueryResult> findAdvertisementsWithDonationCountByIdInAndStatus(
//            @Param("adIds") List<Long> adIds, @Param("status") Status status);
//    @Query(value = "SELECT ad.*, ar.name AS artist_name, COUNT(d.advertisement_id) AS donor_count " +
//            "FROM advertisement ad " +
//            "LEFT JOIN artist ar ON ad.artist_id = ar.artist_id " +
//            "LEFT JOIN donation d ON ad.advertisement_id = d.advertisement_id " +
//            "WHERE ad.advertisement_id IN :adIds AND ad.status = :status " +
//            "GROUP BY ad.advertisement_id", nativeQuery = true)
//    List<AdvertisementDTO.AdQueryResult> findAdvertisementsWithDonationCountByIdInAndStatusNative(
//            @Param("adIds") List<Long> adIds, @Param("status") String status);

    @Query(value = "SELECT " +
            "    ad.advertisement_id AS advertisementId, " +
            "    ad.name AS adTitle, " +
            "    ar.name AS artistName, " +
            "    ad.image_url AS imageUrl, " +
            "    ad.media_type AS mediaType, " +
            "    ad.end_date AS endDate, " +
            "    ad.current_amount AS currentAmount, " +
            "    ad.goal_amount AS goalAmount, " +
            "    COUNT(d.advertisement_id) AS donorCount " +
            "FROM advertisement ad " +
            "LEFT JOIN artist ar ON ad.artist_id = ar.artist_id " +
            "LEFT JOIN donation d ON ad.advertisement_id = d.advertisement_id " +
            "WHERE ad.advertisement_id IN (:adIds) AND ad.status = :status " +
            "GROUP BY ad.advertisement_id, ar.name", nativeQuery = true)
    List<AdQueryResultInterface> findAdvertisementsWithDonationCountByIdInAndStatusNative(
            @Param("adIds") List<Long> adIds, @Param("status") String status);
}
