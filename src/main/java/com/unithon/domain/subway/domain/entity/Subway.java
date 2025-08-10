package com.unithon.domain.subway.domain.entity;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subway extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_station_id")
    private SubwayStation subwayStation;

    private int lineCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade; // SSA, SA, A

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // A, B

    private String placement;

    private int price;
    private int sizeWidthCm;
    private int sizeHeightCm;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    public void assign(Advertisement ad) { this.advertisement = ad; }
}
