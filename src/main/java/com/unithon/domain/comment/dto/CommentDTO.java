package com.unithon.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    /**
     * 댓글 작성 요청(Request)을 위한 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRequest {
        private String context; // 댓글 내용
    }

    /**
     * 댓글 작성 성공 후 응답(Response)을 위한 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long commentId;
        private Long advertisementId;
        private String userEmail; // 작성자 이메일 (마스킹 처리 가능)
        private String context;
        private LocalDateTime createdAt;
    }

    /**
     * 댓글 목록 조회 API의 최종 응답 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentListResponse {
        private List<CommentOneResponse> comments; // 댓글 목록
        private Pagination pagination;          // 페이지네이션 정보
    }

    /**
     * 단일 댓글 정보를 담는 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentOneResponse {
        private Long commentId;
        private String userEmail; // 작성자 이메일
        private String context;
        private LocalDateTime createdAt;
    }

    /**
     * 페이지네이션 정보를 담는 DTO
     */
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pagination {
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean isLast; // 마지막 페이지 여부
    }
}
