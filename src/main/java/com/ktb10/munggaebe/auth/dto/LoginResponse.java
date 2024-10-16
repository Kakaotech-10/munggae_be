package com.ktb10.munggaebe.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private Long id;
    private String nickname;
    private AuthTokens token;

    public LoginResponse(Long id, String nickname, AuthTokens token) {
        this.id = id;
        this.nickname = nickname;
        this.token = token;
    }
}
