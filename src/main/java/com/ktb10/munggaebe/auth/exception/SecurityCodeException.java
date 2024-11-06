package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import com.ktb10.munggaebe.error.ErrorCodeException;

public class SecurityCodeException extends ErrorCodeException {
    public SecurityCodeException() {
        super(ErrorCode.JWT_SECURITY_EXCEPTION);
    }
}
