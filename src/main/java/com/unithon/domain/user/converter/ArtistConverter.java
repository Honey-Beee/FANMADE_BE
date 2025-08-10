package com.unithon.domain.user.converter;

import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.dto.ArtistDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistConverter {

    public static ArtistDTO.ArtistResponse toArtistResponse(Artist artist) {
        return ArtistDTO.ArtistResponse.builder()
                .artistId(artist.getId())
                .name(artist.getName())
                .imageUrl(artist.getImageUrl())
                .build();
    }

    public static List<ArtistDTO.ArtistResponse> toArtistResponseList(List<Artist> artistList) {
        return artistList.stream()
                .map(ArtistConverter::toArtistResponse)
                .collect(Collectors.toList());
    }

    public static ArtistDTO.RecommendArtistResponse toRecommendArtistResponse(Artist artist) {
        if (artist == null) return null;
        return ArtistDTO.RecommendArtistResponse.builder()
                .artistId(artist.getId())
                .name(artist.getName())
                .imageUrl(artist.getImageUrl())
                .build();
    }

    public static List<ArtistDTO.RecommendArtistResponse> toRecommendArtistResponseList(List<Artist> artistList) {
        return artistList == null ? List.of()
                : artistList.stream()
                .map(ArtistConverter::toRecommendArtistResponse)
                .collect(Collectors.toList());
    }
}