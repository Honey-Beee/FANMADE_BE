package com.unithon.domain.advertisement.domain.repository;

public interface AdQueryResultInterface {
    Long getAdvertisementId();
    String getAdTitle();
    String getArtistName();
    String getImageUrl();
    String getMediaType(); // MediaType도 String으로 받습니다.
    java.time.LocalDate getEndDate();
    Integer getCurrentAmount();
    Integer getGoalAmount();
    Long getDonorCount();
}