package com.unithon.domain.subway.domain.repository;

import com.unithon.domain.subway.domain.entity.Subway;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubwayRepository extends JpaRepository<Subway, Long> {

    @Query("""
       select s from Subway s
       where s.advertisement is null
         and s.price <= :budget
       order by s.price desc
    """)
    List<Subway> findWithinBudget(@Param("budget") int budget);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Subway s where s.id = :id")
    Optional<Subway> findByIdForUpdate(@Param("id") Long id);

    boolean existsByAdvertisement_AdvertisementId(Long adId);

    @Query("select s from Subway s where s.advertisement.advertisementId = :adId")
    Subway findByAdvertisementId(@Param("adId") Long adId);
}
