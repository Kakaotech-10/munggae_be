package com.ktb10.munggaebe.auth.dto;

import lombok.Getter;

@Getter
public class AuthTokens {

    private String accessToken;
    private String refreshToken;
    private String grantType;
    private Long expiresIn;

    public AuthTokens(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.grantType = grantType;
        this.expiresIn = expiresIn;
    }

    public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
        return new AuthTokens(accessToken, refreshToken, grantType, expiresIn);
    }
}
