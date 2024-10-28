package com.ktb10.munggaebe.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 응답")
public class LoginResponse {

    @Schema(description = "사용자 카카오 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 닉네임", example = "김요한")
    private String nickname;

    @Schema(description = "엑세스 토큰 정보")
    private AccessTokenResponse token;

    public LoginResponse(Long id, String nickname, AccessTokenResponse token) {
        this.id = id;
        this.nickname = nickname;
        this.token = token;
    }
}
