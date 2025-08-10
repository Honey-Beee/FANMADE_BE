package com.unithon.domain.comment.domain.repository;

import com.unithon.domain.comment.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 특정 광고의 댓글 목록을 페이지 단위로 조회합니다.
     * JOIN FETCH를 사용하여 댓글 작성자(User) 정보를 함께 가져와 N+1 문제를 해결합니다.
     * @param advertisementId 광고 ID
     * @param pageable 페이지네이션 정보 (페이지 번호, 페이지 크기, 정렬 순서)
     * @return Comment 엔티티를 담은 Page 객체
     */
    @Query(value = "SELECT c FROM Comment c JOIN FETCH c.user " +
            "WHERE c.advertisement.advertisementId = :advertisementId",
            countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.advertisement.advertisementId = :advertisementId")
    Page<Comment> findByAdvertisementIdWithUser(
            @Param("advertisementId") Long advertisementId, Pageable pageable);
}
