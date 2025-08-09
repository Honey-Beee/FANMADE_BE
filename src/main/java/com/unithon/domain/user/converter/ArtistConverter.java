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
}