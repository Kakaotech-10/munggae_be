package com.ktb10.munggaebe.auth.exception;

import com.ktb10.munggaebe.error.ErrorCode;
import com.ktb10.munggaebe.error.ErrorCodeException;

public class MalformedTokenCodeException extends ErrorCodeException {
    public MalformedTokenCodeException() {
        super(ErrorCode.JWT_MALFORMED_TOKEN);
    }
}
