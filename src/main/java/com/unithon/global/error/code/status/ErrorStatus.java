package com.unithon.global.error.code.status;

import com.unithon.global.error.code.BaseErrorCode;
import com.unithon.global.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // advertisement
    AD_NOT_FOUND(HttpStatus.NOT_FOUND, "AD404", "광고를 찾을 수 없습니다."),
    INVALID_FUNDING_GOAL(HttpStatus.BAD_REQUEST, "AD4001", "목표 금액은 최소 100,000원 이상이어야 합니다."),
    INVALID_FUNDING_PERIOD(HttpStatus.BAD_REQUEST, "AD4002", "종료일은 시작일 이후여야 합니다."),
    INVALID_BUDGET(HttpStatus.BAD_REQUEST, "AD4003", "budget 값이 유효하지 않습니다."),
    INVALID_MEDIA_TYPE(HttpStatus.BAD_REQUEST, "AD4004", "mediaType은 SUBWAY 또는 BUS여야 합니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "AD4041", "선택한 광고 매체를 찾을 수 없습니다."),
    PLACE_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "AD4091", "이미 다른 광고에 배정된 매체입니다."),
    AD_ALREADY_HAS_PLACE(HttpStatus.CONFLICT, "AD4092", "해당 광고에 이미 배정된 매체가 있습니다."),
    AD_STATUS_INVALID(HttpStatus.BAD_REQUEST, "AD4005", "현재 상태에서는 장소를 배정할 수 없습니다.");



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
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(httpStatus)
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }
}
