package com.unithon.domain.user.domain.repository;

import com.unithon.domain.user.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    /**
     * [기존 메서드 - 참고용]
     * 아티스트 이름(name)을 대소문자 구분 없이, 포함(containing)하는 모든 아티스트를 찾습니다.
     * 예: "b"로 검색 시 "BTS", "BLACKPINK" 모두 조회
     * @param keyword 검색어
     * @return 아티스트 엔티티 리스트
     */
    // List<Artist> findByNameContainingIgnoreCase(String keyword);

    /**
     * [수정된 메서드]
     * 아티스트의 이름(name) 또는 그룹명(groupName)에 키워드가 포함된 모든 아티스트를 검색합니다. (대소문자 무시)
     *
     * @param nameKeyword 멤버 이름(name)에서 찾을 키워드
     * @param groupNameKeyword 그룹 이름(groupName)에서 찾을 키워드
     * @return 검색 조건에 맞는 아티스트 엔티티 리스트
     */
    List<Artist> findByNameContainingIgnoreCaseOrGroupNameContainingIgnoreCase(String nameKeyword, String groupNameKeyword);
}
