package com.unithon.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ArtistDTO {

    /**
     * 아티스트 검색 결과 응답을 위한 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistResponse {
        private Long artistId;
        private String name;
        private String imageUrl;
        private String groupName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RecommendArtistResponse {
        private Long artistId;
        private String name;
        private String imageUrl;
        private String groupName;
    }
}
