package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import com.ktb10.munggaebe.error.ErrorCodeException;

public class IllegalTokenCodeException extends ErrorCodeException {
    public IllegalTokenCodeException() {
        super(ErrorCode.JWT_ILLEGAL_TOKEN);
    }
}
