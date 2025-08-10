package com.unithon.domain.comment.controller;

import com.unithon.domain.comment.application.CommentService;
import com.unithon.domain.comment.dto.CommentDTO;
import com.unithon.domain.user.domain.entity.User;
import com.unithon.domain.user.domain.repository.UserRepository;
import com.unithon.global.common.BaseResponse;
import com.unithon.global.error.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("advertisements/{advertisementId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    /**
     * 특정 광고에 댓글 작성 (인증 필요)
     */
    @PostMapping
    public BaseResponse<CommentDTO.CommentResponse> createComment(
            @PathVariable Long advertisementId,
            @RequestBody CommentDTO.CommentRequest requestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 1. 인증 정보에서 이메일을 가져와 User 엔티티 조회
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("인증된 사용자를 찾을 수 없습니다."));

        // 2. 서비스 로직 호출
        CommentDTO.CommentResponse response =
                commentService.createComment(user.getId(), advertisementId, requestDTO);

        // 3. 성공 응답 반환
        return BaseResponse.onSuccess(SuccessStatus.COMMENT_CREATE_SUCCESS, response);
    }

    /**
     * 특정 광고의 댓글 목록 조회 (인증 불필요)
     */
    @GetMapping
    public BaseResponse<CommentDTO.CommentListResponse> getComments(
            @PathVariable Long advertisementId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 1. 페이지네이션 및 정렬 정보 생성 (최신 댓글이 위로 오도록)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 2. 서비스 로직 호출
        CommentDTO.CommentListResponse response = commentService.getComments(advertisementId, pageable);

        // 3. 성공 응답 반환
        return BaseResponse.onSuccess(SuccessStatus.COMMENT_LIST_SUCCESS, response);
    }
}
