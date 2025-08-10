package com.unithon.domain.bus.domain.repository;

import com.unithon.domain.bus.domain.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {

    @Query("""
       select b from Bus b
       where b.advertisement is null
         and b.price <= :budget
       order by b.price desc
    """)
    List<Bus> findWithinBudget(@Param("budget") int budget);
}