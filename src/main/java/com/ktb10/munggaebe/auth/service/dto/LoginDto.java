package com.ktb10.munggaebe.auth.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginDto {
    private Long memberId;
    private Long kakaoId;
    private String nickname;
    private boolean isMemberJoin;
    private AccessTokenResponse accessToken;
    private RefreshTokenResponse refreshToken;

    @Builder
    public LoginDto(Long memberId, Long kakaoId, String nickname, boolean isMemberJoin, AccessTokenResponse accessToken, RefreshTokenResponse refreshToken) {
        this.memberId = memberId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.isMemberJoin = isMemberJoin;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
