package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import com.ktb10.munggaebe.error.ErrorCodeException;

public class InvalidSignatureCodeException extends ErrorCodeException {
    public InvalidSignatureCodeException() {
        super(ErrorCode.JWT_INVALID_SIGNATURE);
    }
}
