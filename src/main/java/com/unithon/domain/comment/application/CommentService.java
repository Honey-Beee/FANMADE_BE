package com.unithon.domain.comment.application;

import com.unithon.domain.comment.dto.CommentDTO;

public interface CommentService {
    public CommentDTO.CommentResponse createComment(
            Long userId, Long advertisementId, CommentDTO.CommentRequest requestDTO);
}
