package com.ktb10.munggaebe.post.exception;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private long id;

    public CommentNotFoundException(long id) {
        super("id " + id + " 에 해당하는 Comment가 존재하지 않습니다.");
        this.id = id;
    }
}
