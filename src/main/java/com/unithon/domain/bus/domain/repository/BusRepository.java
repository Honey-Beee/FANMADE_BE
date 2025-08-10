package com.unithon.domain.bus.domain.repository;

import com.unithon.domain.bus.domain.entity.Bus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {

    @Query("""
       select b from Bus b
       where b.advertisement is null
         and b.price <= :budget
       order by b.price desc
    """)
    List<Bus> findWithinBudget(@Param("budget") int budget);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Bus s where s.id = :id")
    Optional<Bus> findByIdForUpdate(@Param("id") Long id);

    boolean existsByAdvertisement_AdvertisementId(Long adId);
}