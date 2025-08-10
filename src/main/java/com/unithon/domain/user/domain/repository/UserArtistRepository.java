package com.unithon.domain.user.domain.repository;

import com.unithon.domain.user.domain.entity.Artist;
import com.unithon.domain.user.domain.entity.UserArtist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserArtistRepository extends JpaRepository<UserArtist, Long> {

    @Query("""
           select ua.artist
           from UserArtist ua
           where ua.user.id = :userId
           order by ua.updatedAt desc
           """)
    List<Artist> findRecentLikedArtists(@Param("userId") Long userId, Pageable pageable);
}
