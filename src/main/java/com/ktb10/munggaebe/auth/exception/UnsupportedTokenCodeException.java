package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import com.ktb10.munggaebe.error.ErrorCodeException;

public class UnsupportedTokenCodeException extends ErrorCodeException {
    public UnsupportedTokenCodeException() {
        super(ErrorCode.JWT_UNSUPPORTED_TOKEN);
    }
}
