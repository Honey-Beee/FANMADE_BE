package com.unithon.domain.advertisement.domain.repository;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.entity.Status;
import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    // --- 광고 목록 조회를 위한 기본 메서드 ---
    // status 필터링이 필요하므로, 상태별로 페이지를 찾는 메서드를 정의합니다.
    Page<Advertisement> findByStatus(Status status, Pageable pageable);

    // --- 요약 정보 조회를 위한 메서드 ---
    // [수정] 반환 타입을 List<Object[]> 로 변경
    // [수정] 총 모금액을 ad.currentAmount의 합으로 변경, [총광고수, 모금한사람, 총모금액]
    @Query("SELECT COUNT(ad.advertisementId), COUNT(DISTINCT d.user.id), COALESCE(SUM(ad.currentAmount), 0) " +
            "FROM Advertisement ad LEFT JOIN Donation d ON d.advertisement = ad")
    List<Object[]> findSummaryInfoAll();

    // 2. 상태별 요약 정보
    @Query("SELECT COUNT(ad.advertisementId), COUNT(DISTINCT d.user.id), COALESCE(SUM(ad.currentAmount), 0) " +
            "FROM Advertisement ad LEFT JOIN Donation d ON d.advertisement = ad WHERE ad.status = :status")
    List<Object[]> findSummaryInfoByStatus(@Param("status") Status status);

    // --- [중요] 후원자 수 N+1 문제 해결을 위한 쿼리 ---
    /**
     * 주어진 광고 ID 목록에 해당하는 각 광고의 후원자 수를 조회합니다.
     * @param adIds 광고 ID 리스트
     * @return [광고 ID, 후원자 수] 형태의 Object 배열 리스트
     */
    @Query("SELECT d.advertisement.advertisementId, COUNT(d.id) " +
            "FROM Donation d " +
            "WHERE d.advertisement.advertisementId IN :adIds " +
            "GROUP BY d.advertisement.advertisementId")
    List<Object[]> findDonorCountsByAdvertisementIds(@Param("adIds") List<Long> adIds);


    /**
     * 광고 상세 정보와 해당 광고의 총 후원자 수를 한 번의 쿼리로 조회
     * @param advertisementId 조회할 광고의 ID
     * @return Advertisement 엔티티와 Long 타입의 후원자 수를 담은 Object 배열
     */
    @Query("SELECT ad, COUNT(d.id) " +
            "FROM Advertisement ad " +
            "LEFT JOIN ad.artistId " +
            "LEFT JOIN Donation d ON d.advertisement = ad " +
            "WHERE ad.advertisementId = :advertisementId " +
            "GROUP BY ad.advertisementId")
    Optional<Object[]> findAdvertisementWithDonorCountById(@Param("advertisementId") Long advertisementId);
}
