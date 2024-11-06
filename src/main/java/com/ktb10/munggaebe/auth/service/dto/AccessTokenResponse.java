package com.ktb10.munggaebe.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(description = "엑세스 토큰 응답")
public class AccessTokenResponse {

    @Schema(description = "엑세스 토큰", example = "eyJhbGciOi...")
    private String accessToken;

    @Schema(description = "토큰 유형", example = "Bearer")
    private String grantType;

    @Schema(description = "엑세스 토큰 만료 시간(초)", example = "3600")
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
