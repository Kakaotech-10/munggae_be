package com.ktb10.munggaebe.error;

import lombok.Getter;

@Getter
public abstract class ErrorCodeException extends RuntimeException {

    private ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
