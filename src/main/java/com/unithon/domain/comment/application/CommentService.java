package com.unithon.domain.comment.application;

import com.unithon.domain.comment.dto.CommentDTO;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    public CommentDTO.CommentResponse createComment(
            Long userId, Long advertisementId, CommentDTO.CommentRequest requestDTO);

    public CommentDTO.CommentListResponse getComments(Long advertisementId, Pageable pageable);
}
