package com.unithon.domain.user.domain.repository;

import com.unithon.domain.user.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    /**
     * 아티스트 이름(name)을 대소문자 구분 없이, 포함(containing)하는 모든 아티스트를 찾습니다.
     * 예: "b"로 검색 시 "BTS", "BLACKPINK" 모두 조회
     * @param keyword 검색어
     * @return 아티스트 엔티티 리스트
     */
    List<Artist> findByNameContainingIgnoreCase(String keyword);
}
