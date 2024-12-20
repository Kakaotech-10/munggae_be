package com.ktb10.munggaebe.post.exception;

import lombok.Getter;

@Getter
public class PermissionException extends RuntimeException {

    public PermissionException() { super("게시글을 작성할 수 있는 권한이 없습니다."); }
}
