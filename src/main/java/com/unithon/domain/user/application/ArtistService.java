package com.unithon.domain.user.application;

import com.unithon.domain.user.dto.ArtistDTO;

import java.util.List;

public interface ArtistService {
    public List<ArtistDTO.ArtistResponse> searchArtists(String keyword);
    List<ArtistDTO.RecommendArtistResponse> getMyRecommendedArtists();
}
