package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import com.ktb10.munggaebe.error.ErrorCodeException;

public class ExpiredTokenCodeException extends ErrorCodeException {
    public ExpiredTokenCodeException() {
        super(ErrorCode.JWT_EXPIRED_TOKEN);
    }
}
