package com.ktb10.munggaebe.auth.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RefreshTokenResponse {

    private String refreshToken;
    private Long expiresIn;

    public RefreshTokenResponse(String refreshToken, Long expiresIn) {
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public static RefreshTokenResponse of(String refreshToken, Long expiresIn) {
        return new RefreshTokenResponse(refreshToken, expiresIn);
    }
}
