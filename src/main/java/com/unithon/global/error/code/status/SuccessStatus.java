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
    ADVERTISEMENT_LIST_SUCCESS(HttpStatus.OK, "ADVERTISEMENT_2001", "현재 FUNDING중인 광고리스트 조회 성공")

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
