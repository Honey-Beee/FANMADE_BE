package com.unithon.domain.user.application;

import com.unithon.domain.user.converter.ArtistConverter;
import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.domain.repository.ArtistRepository;
import com.unithon.domain.user.dto.ArtistDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    @Override
    public List<ArtistDTO.ArtistResponse> searchArtists(String keyword) {
        // 1. Repository를 호출하여 키워드에 맞는 아티스트 목록 조회
        List<Artist> artistList = artistRepository.findByNameContainingIgnoreCase(keyword);

        // 2. Converter를 사용하여 DTO 리스트로 변환 후 반환
        return ArtistConverter.toArtistResponseList(artistList);
    }
}
