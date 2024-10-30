package com.ktb10.munggaebe.auth.service.dto;

import com.ktb10.munggaebe.auth.dto.AccessTokenResponse;
import com.ktb10.munggaebe.auth.dto.RefreshTokenResponse;
import lombok.Getter;

@Getter
public class LoginDto {
    private Long memberId;
    private Long kakaoId;
    private String nickname;
    private AccessTokenResponse accessToken;
    private RefreshTokenResponse refreshToken;

    public LoginDto(Long memberId, Long kakaoId, String nickname, AccessTokenResponse accessToken, RefreshTokenResponse refreshToken) {
        this.memberId = memberId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
