package com.ktb10.munggaebe.auth.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AccessTokenResponse {

    private String accessToken;
    private String grantType;
    private Long expiresIn;

    public AccessTokenResponse(String accessToken, String grantType, Long expiresIn) {
        this.accessToken = accessToken;
        this.grantType = grantType;
        this.expiresIn = expiresIn;
    }

    public static AccessTokenResponse of(String accessToken, String grantType, Long expiresIn) {
        return new AccessTokenResponse(accessToken, grantType, expiresIn);
    }
}
