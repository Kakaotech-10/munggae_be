package com.ktb10.munggaebe.post.exception;

import lombok.Getter;

@Getter
public class FastApiServerException extends RuntimeException {

    public FastApiServerException() {
        super("FastApi 서버 측 오류입니다.");
    }
}
