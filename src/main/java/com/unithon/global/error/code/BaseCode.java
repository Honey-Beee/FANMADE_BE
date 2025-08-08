package com.unithon.global.error.code;

public interface BaseCode {

    String getCode();

    String getMessage();

    ReasonDTO getReasonHttpStatus();
}
