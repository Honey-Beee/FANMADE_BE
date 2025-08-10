package com.unithon.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    public static class CommentResponse {
        private Long commentId;
        private Long advertisementId;
        private String userEmail; // 작성자 이메일 (마스킹 처리 가능)
        private String context;
        private LocalDateTime createdAt;
    }
}
