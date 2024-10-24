package com.ktb10.munggaebe.auth.service.dto;

import com.ktb10.munggaebe.auth.dto.AccessTokenResponse;
import com.ktb10.munggaebe.auth.dto.RefreshTokenResponse;
import lombok.Getter;

@Getter
public class LoginDto {
    private Long id;
    private String nickname;
    private AccessTokenResponse accessToken;
    private RefreshTokenResponse refreshToken;

    public LoginDto(Long id, String nickname, AccessTokenResponse accessToken, RefreshTokenResponse refreshToken) {
        this.id = id;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
