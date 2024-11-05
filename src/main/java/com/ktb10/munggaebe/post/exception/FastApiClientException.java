package com.ktb10.munggaebe.post.exception;

import lombok.Getter;

@Getter
public class FastApiClientException extends RuntimeException {

    public FastApiClientException() {
        super("잘못된 요청입니다.");
    }
}
