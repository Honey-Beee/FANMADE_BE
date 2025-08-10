package com.unithon.domain.subway.domain.repository;

import com.unithon.domain.subway.domain.entity.Subway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubwayRepository extends JpaRepository<Subway, Long> {

    @Query("""
       select s from Subway s
       where s.advertisement is null
         and s.price <= :budget
       order by s.price desc
    """)
    List<Subway> findWithinBudget(@Param("budget") int budget);
}
