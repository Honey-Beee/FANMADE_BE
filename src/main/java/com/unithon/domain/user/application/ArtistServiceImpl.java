package com.unithon.domain.user.application;

import com.unithon.domain.user.converter.ArtistConverter;
import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.ArtistRepository;
import com.unithon.domain.user.domain.repository.UserArtistRepository;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.domain.user.dto.ArtistDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final UserArtistRepository userArtistRepository;
    private final UserRepository userRepository;

    @Override
    public List<ArtistDTO.ArtistResponse> searchArtists(String keyword) {
        // 1. Repository를 호출하여 키워드에 맞는 아티스트 목록 조회
        List<Artist> artistList = artistRepository.findByNameContainingIgnoreCaseOrGroupNameContainingIgnoreCase(keyword, keyword);

        // 2. Converter를 사용하여 DTO 리스트로 변환 후 반환
        return ArtistConverter.toArtistResponseList(artistList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistDTO.RecommendArtistResponse> getMyRecommendedArtists() {
        Long userId = currentUserId();
        List<Artist> liked = userArtistRepository
                .findRecentLikedArtists(userId, PageRequest.of(0, 6));

        return ArtistConverter.toRecommendArtistResponseList(liked);
    }

    public Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new RuntimeException("인증 필요");

        Object principal = auth.getPrincipal();
        String email = (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : String.valueOf(principal);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음: " + email));
        return user.getId();
    }
}
