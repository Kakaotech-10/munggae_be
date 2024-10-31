package com.ktb10.munggaebe.auth.dto;

import com.ktb10.munggaebe.auth.service.dto.LoginDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
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

    @Schema(description = "사용자 회원가입 여부, true이면 회원가입, false이면 로그인", example = "true")
    private boolean isMemberJoin;

    @Schema(description = "엑세스 토큰 정보")
    private AccessTokenResponse token;

    @Builder
    public LoginResponse(Long memberId, Long kakaoId, String nickname, boolean isMemberJoin, AccessTokenResponse token) {
        this.memberId = memberId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.isMemberJoin = isMemberJoin;
        this.token = token;
    }

    public LoginResponse(LoginDto dto) {
        this.memberId = dto.getMemberId();
        this.kakaoId = dto.getKakaoId();
        this.nickname = dto.getNickname();
        this.isMemberJoin = dto.isMemberJoin();
        this.token = dto.getAccessToken();
    }
}
