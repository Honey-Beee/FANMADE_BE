package com.unithon.domain.advertisement.domain.entity;

import com.unithon.domain.advertisement.dto.AdvertisementDTO;
import com.unithon.domain.bus.domain.entity.Bus;
import com.unithon.domain.model.BaseEntity;
import com.unithon.domain.subway.domain.entity.Subway;
import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advertisement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id")
    private Long advertisementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    private String descriptionKorea;
    private String descriptionEnglish;
    private String descriptionChina;
    private String descriptionJapan;
    private String imageUrl;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status; // FUNDING, FUNDED, CANCELED

    private int goalAmount;
    private int currentAmount;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private Purpose purpose; // 데뷔, 생일, 컴백, 기타

    /**
     * 이 광고에 연결된 Subway 정보입니다.
     */
    @OneToOne(mappedBy = "advertisement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Subway subway;

    /**
     * 이 광고에 연결된 Bus 정보입니다.
     */
    @OneToOne(mappedBy = "advertisement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Bus bus;

    public void addCurrentAmount(Long amount) {
        this.currentAmount += amount;
    }

    public void changeImageUrl(String url) { this.imageUrl = url; }

}
