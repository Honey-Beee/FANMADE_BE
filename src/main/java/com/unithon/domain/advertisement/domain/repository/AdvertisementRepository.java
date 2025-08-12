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
    // --- [수정] N+1 문제 해결을 위해 모든 연관 엔티티를 JOIN FETCH ---
    @Query(value = "SELECT ad FROM Advertisement ad " +
            "JOIN FETCH ad.artistId " +
            "LEFT JOIN FETCH ad.subway s " + // Subway 정보 함께 로드
            "LEFT JOIN FETCH s.subwayStation " + // Subway의 역 정보도 함께 로드
            "LEFT JOIN FETCH ad.bus", // Bus 정보 함께 로드
            countQuery = "SELECT COUNT(ad) FROM Advertisement ad")
    @Override
    Page<Advertisement> findAll(Pageable pageable);

    // --- 광고 목록 조회를 위한 기본 메서드 ---
    // status 필터링이 필요 -> 수정
    @Query(value = "SELECT ad FROM Advertisement ad " +
            "JOIN FETCH ad.artistId " +
            "LEFT JOIN FETCH ad.subway s " +
            "LEFT JOIN FETCH s.subwayStation " +
            "LEFT JOIN FETCH ad.bus " +
            "WHERE ad.status = :status",
            countQuery = "SELECT COUNT(ad) FROM Advertisement ad WHERE ad.status = :status")
    Page<Advertisement> findByStatus(Status status, Pageable pageable);

    // --- 요약 정보 조회를 위한 메서드 ---
    // [수정] 반환 타입을 List<Object[]> 로 변경
    // [수정] 총 모금액을 ad.currentAmount의 합으로 변경, [총광고수, 모금한사람, 총모금액]

    @Query("SELECT COUNT(DISTINCT ad.advertisementId), COUNT(DISTINCT d.user.id), COALESCE(SUM(ad.currentAmount), 0) " +
            "FROM Advertisement ad LEFT JOIN Donation d ON d.advertisement = ad")
    List<Object[]> findSummaryInfoAll();

    // 2. 상태별 요약 정보

    // [수정] COUNT(ad.advertisementId) -> COUNT(DISTINCT ad.advertisementId)
    @Query("SELECT COUNT(DISTINCT ad.advertisementId), COUNT(DISTINCT d.user.id), COALESCE(SUM(ad.currentAmount), 0) " +
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
     * [수정] 광고 상세 정보 조회 시, 위치 정보(Subway, Bus)까지 JOIN FETCH로 함께 가져옵니다.
     */
    @Query("SELECT ad, COUNT(d.id) " +
            "FROM Advertisement ad " +
            "LEFT JOIN FETCH ad.artistId " +
            "LEFT JOIN FETCH ad.subway s " +
            "LEFT JOIN FETCH s.subwayStation " +
            "LEFT JOIN FETCH ad.bus " +
            "LEFT JOIN Donation d ON d.advertisement = ad " +
            "WHERE ad.advertisementId = :advertisementId " +
            "GROUP BY ad.advertisementId")
    Optional<Object[]> findAdvertisementWithDonorCountById(@Param("advertisementId") Long advertisementId);

    // 소유자 확인용 메서드 추가
    Optional<Advertisement> findByAdvertisementIdAndUser_Id(Long advertisementId, Long userId);
}
