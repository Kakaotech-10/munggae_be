package com.ktb10.munggaebe.member.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

    private long id;

    public MemberNotFoundException(long id) {
        super("id " + id + " 에 해당하는 Member가 존재하지 않습니다.");
        this.id = id;
    }
}
