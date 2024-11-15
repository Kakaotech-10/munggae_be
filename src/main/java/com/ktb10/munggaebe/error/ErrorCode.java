package com.ktb10.munggaebe.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //common
    INTERNAL_SERVER_ERROR("COM_001", "서버 측 오류가 발생했습니다.", 500),
    ILLEGAL_ARGUMENT_EXCEPTION("COM_002", "클라이언트의 잘못된 요청입니다.", 400),

    //member
    MEMBER_NOT_FOUND("MEM_001", "해당하는 맴버를 찾을 수 없습니다.", 404),
    MEMBER_PERMISSION_DENIED_EXCEPTION("MEM_002","해당 맴버는 권한이 없습니다.", 403),

    //post
    POST_NOT_FOUND("POS_001", "해당하는 게시물을 찾을 수 없습니다.", 404),
    COMMENT_NOT_FOUND("POS_002", "해당하는 댓글을 찾을 수 없습니다.", 404),

    //OAuth
    OAUTH_LOGIN_ERROR("OAU_001", "로그인 과정에서 인증 오류가 발생하였습니다.", 500),

    //JWT
    JWT_INVALID_SIGNATURE("JWT_001", "유효하지 않은 JWT 서명입니다.", 401),
    JWT_MALFORMED_TOKEN("JWT_002", "잘못된 형식의 JWT 토큰입니다.", 400),
    JWT_EXPIRED_TOKEN("JWT_003", "만료된 JWT 토큰입니다.", 401),
    JWT_UNSUPPORTED_TOKEN("JWT_004", "지원되지 않는 JWT 토큰입니다.", 400),
    JWT_ILLEGAL_TOKEN("JWT_005", "허용되지 않는 JWT 토큰입니다.", 400),
    JWT_SECURITY_EXCEPTION("JWT_006", "보안 예외가 발생했습니다.", 403);
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
