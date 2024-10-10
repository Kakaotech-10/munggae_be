package com.ktb10.munggaebe.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //common
    INTERNAL_SERVER_ERROR("COM-001", "서버 측 오류가 발생했습니다.", 500)
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
