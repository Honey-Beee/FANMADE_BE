package com.unithon.domain.comment.converter;

import com.unithon.domain.comment.domain.entity.Comment;
import com.unithon.domain.comment.dto.CommentDTO;

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
}
