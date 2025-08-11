package com.unithon.domain.bus.domain.entity;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusType busType; // A형, B형, C형, D형

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Route route;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FaceType face; // 차도면, 인도면, 후면 등

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade; // SSA, SA, A

    private int price;
    private Integer sizeWidthCm;
    private Integer sizeHeightCm;
    private String busNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    public void assign(Advertisement ad) { this.advertisement = ad; }
}
