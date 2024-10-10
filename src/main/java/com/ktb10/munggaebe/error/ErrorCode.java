package com.ktb10.munggaebe.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //common
    INTERNAL_SERVER_ERROR("COM_001", "서버 측 오류가 발생했습니다.", 500),

    //member
    MEMBER_NOT_FOUND("MEM_001", "해당하는 맴버를 찾을 수 없습니다.", 404),
    MEMBER_PERMISSION_DENIED_EXCEPTION("MEM_002","해당 맴버는 권한이 없습니다.", 403),

    //post
    POST_NOT_FOUND("POS_001", "해당하는 게시물을 찾을 수 없습니다.", 404)
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
