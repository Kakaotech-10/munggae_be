package com.ktb10.munggaebe.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "로그인 응답")
public class LoginResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long memberId;

    @Schema(description = "사용자 카카오 ID", example = "31231231")
    private Long kakaoId;

    @Schema(description = "사용자 닉네임", example = "김요한")
    private String nickname;

    @Schema(description = "엑세스 토큰 정보")
    private AccessTokenResponse token;

    public LoginResponse(Long memberId, Long kakaoId, String nickname, AccessTokenResponse token) {
        this.memberId = memberId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.token = token;
    }
}
