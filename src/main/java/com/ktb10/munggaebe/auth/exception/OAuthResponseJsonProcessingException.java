package com.ktb10.munggaebe.auth.exception;

import lombok.Getter;

@Getter
public class OAuthResponseJsonProcessingException extends RuntimeException {

    private String responseBody;

    public OAuthResponseJsonProcessingException(String responseBody) {
        super("Json 변환 과정에서 예외 발생. OAuth response: " + responseBody);
        this.responseBody = responseBody;
    }
}
