package com.unithon.domain.subway.domain.entity;

import com.unithon.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubwayStation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_station_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
