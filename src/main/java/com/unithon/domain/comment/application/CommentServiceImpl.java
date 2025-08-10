package com.unithon.domain.comment.application;

import com.unithon.domain.advertisement.domain.entity.Advertisement;
import com.unithon.domain.advertisement.domain.repository.AdvertisementRepository;
import com.unithon.domain.comment.converter.CommentConverter;
import com.unithon.domain.comment.domain.entity.Comment;
import com.unithon.domain.comment.domain.repository.CommentRepository;
import com.unithon.domain.comment.dto.CommentDTO;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;


    @Override
    public CommentDTO.CommentResponse createComment(Long userId, Long advertisementId, CommentDTO.CommentRequest requestDTO) {

        // 1. 댓글을 달기 위한 User와 Advertisement 엔티티를 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + userId));

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new RuntimeException("광고를 찾을 수 없습니다. ID: " + advertisementId));

        // 2. 새로운 Comment 엔티티 생성
        Comment newComment = Comment.builder()
                .user(user)
                .advertisement(advertisement)
                .context(requestDTO.getContext())
                .build();

        // 3. Repository를 통해 DB에 저장 (INSERT)
        commentRepository.save(newComment);

        // 4. 저장된 엔티티를 Converter를 통해 응답 DTO로 변환하여 반환
        return CommentConverter.toCommentResponse(newComment);
    }

    @Override
    public CommentDTO.CommentListResponse getComments(Long advertisementId, Pageable pageable) {

            // 1. Repository를 호출하여 댓글 Page를 조회 (User 정보가 함께 로드됨)
            Page<Comment> commentPage = commentRepository.findByAdvertisementIdWithUser(advertisementId, pageable);

            // 2. Converter를 사용하여 최종 응답 DTO로 변환 후 반환
            return CommentConverter.toCommentListResponse(commentPage);

    }
}
