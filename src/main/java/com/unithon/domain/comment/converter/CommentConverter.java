package com.unithon.domain.comment.converter;

import com.unithon.domain.comment.domain.entity.Comment;
import com.unithon.domain.comment.dto.CommentDTO;
import com.unithon.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {
    public static CommentDTO.CommentResponse toCommentResponse(Comment comment) {

        // 필요하다면 여기서 이메일 마스킹 로직을 추가할 수 있습니다.
        String userEmail = comment.getUser().getEmail();

        return CommentDTO.CommentResponse.builder()
                .commentId(comment.getId())
                .advertisementId(comment.getAdvertisement().getAdvertisementId())
                .userEmail(userEmail)
                .context(comment.getContext())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    /**
     * Page<Comment> 객체를 CommentListResponse DTO로 변환합니다.
     */
    public static CommentDTO.CommentListResponse toCommentListResponse(Page<Comment> commentPage) {

        // Page 객체에서 Comment 엔티티 리스트를 DTO 리스트로 변환
        List<CommentDTO.CommentOneResponse> commentResponses = commentPage.getContent().stream()
                .map(CommentConverter::toCommentOneResponse)
                .collect(Collectors.toList());

        // Page 객체에서 페이지네이션 정보를 DTO로 변환
        CommentDTO.Pagination pagination = CommentDTO.Pagination.builder()
                .currentPage(commentPage.getNumber())
                .totalPages(commentPage.getTotalPages())
                .totalElements(commentPage.getTotalElements())
                .isLast(commentPage.isLast())
                .build();

        // 최종 응답 DTO 조립
        return CommentDTO.CommentListResponse.builder()
                .comments(commentResponses)
                .pagination(pagination)
                .build();
    }

    /**
     * 단일 Comment 엔티티를 CommentOneResponse DTO로 변환합니다.
     */
    public static CommentDTO.CommentOneResponse toCommentOneResponse(Comment comment) {
        // User 엔티티가 함께 조회되었으므로, 추가 쿼리 없이 바로 사용 가능
        User user = comment.getUser();

        return CommentDTO.CommentOneResponse.builder()
                .commentId(comment.getId())
                .userEmail(user.getEmail()) // 마스킹 없이 원본 이메일 사용
                .context(comment.getContext())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
