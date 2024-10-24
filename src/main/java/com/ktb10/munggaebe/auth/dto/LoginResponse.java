package com.ktb10.munggaebe.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Long id;
    private String nickname;
    private AccessTokenResponse token;

    public LoginResponse(Long id, String nickname, AccessTokenResponse token) {
        this.id = id;
        this.nickname = nickname;
        this.token = token;
    }
}
