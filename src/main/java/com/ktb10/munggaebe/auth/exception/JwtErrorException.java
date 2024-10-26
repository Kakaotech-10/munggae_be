package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import lombok.Getter;

@Getter
public class JwtErrorException extends RuntimeException {

    ErrorCode errorCode;

    public JwtErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
