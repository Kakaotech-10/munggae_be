package com.ktb10.munggaebe.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(description = "리프레시 토큰 응답")
public class RefreshTokenResponse {

    @Schema(description = "리프레시 토큰", example = "dXNlcl9yZWZyZXNoVG9rZW4=")
    private String refreshToken;

    @Schema(description = "리프레시 토큰 만료 시간(초)", example = "2592000")
    private Long expiresIn;

    public RefreshTokenResponse(String refreshToken, Long expiresIn) {
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public static RefreshTokenResponse of(String refreshToken, Long expiresIn) {
        return new RefreshTokenResponse(refreshToken, expiresIn);
    }
}
