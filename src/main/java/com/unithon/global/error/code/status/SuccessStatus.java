package com.unithon.global.error.code.status;

import com.unithon.global.error.code.BaseCode;
import com.unithon.global.error.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    //Common
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),


    USER_SIGNUP_SUCCESS(HttpStatus.OK, "USER_2001", "회원가입 성공"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, "USER_2002", "로그인 성공"),

    // Advertisement
    ADVERTISEMENT_LIST_SUCCESS(HttpStatus.OK, "ADVERTISEMENT_2001", "현재 FUNDING중인 광고리스트 조회 성공"),
    ADVERTISEMENT_DETAIL_SUCCESS(HttpStatus.OK, "ADVERTISEMENT_2002", "광고 디테일 메인 페이지 조회 성공"),
    ADVERTISEMENT_DRAFT_CREATED(HttpStatus.CREATED, "ADVERTISEMENT_2003","드래프트가 생성되었습니다."),
    ADVERTISEMENT_FUNDING_SAVED(HttpStatus.OK, "ADVERTISEMENT_2004","펀딩 정보가 저장되었습니다."),
    PLACEMENT_FILTERED(HttpStatus.OK, "ADVERTISEMENT_2005", "광고 매체가 예산 기준으로 필터링되었습니다."),
    PLACE_CHOSEN(HttpStatus.OK, "ADVERTISEMENT_2006", "광고 매체가 선택되었습니다."),
    ADVERTISEMENT_SUMMARY_SUCCESS(HttpStatus.OK, "ADVERTISEMENT_2007", "광고 최종 요약 조회 성공"),
    ADVERTISEMENT_SUBMITTED(HttpStatus.OK, "ADVERTISEMENT_2006", "광고가 FUNDING 상태로 전환되었습니다."),

    // Donation
    DONATION_SUCCESS(HttpStatus.OK, "DONATION_2001", "후원 등록 성공"),
    TOP_DONOR_LIST_SUCCESS(HttpStatus.OK, "DONATION_4002", "후원자 top3 조회 성공"),

    // Artist
    ARTIST_SEARCH_SUCCESS(HttpStatus.OK, "ARTIST_2001", "아티스트 검색 성공"),
    ARTIST_RECOMMEND_SUCCESS(HttpStatus.OK, "ARTIST201", "아티스트 추천 목록 조회 성공"),

    // Comment

    COMMENT_CREATE_SUCCESS(HttpStatus.OK, "COMMENT_6001", "댓글 등록 성공"),
    COMMENT_LIST_SUCCESS(HttpStatus.OK, "COMMENT_6002", "댓글 조회 성공"),

    COMMENT_CREATE_SUCCESS(HttpStatus.OK, "COMMENT_6001", "댓글 등록 성공")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .httpStatus(httpStatus)
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

}
