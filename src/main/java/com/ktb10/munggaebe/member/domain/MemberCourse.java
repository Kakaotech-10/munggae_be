package com.ktb10.munggaebe.member.domain;

import lombok.Getter;

@Getter
public enum MemberCourse {

    DEFAULT("초기값"),
    FULLSTACK("풀스택"),
    CLOUD("클라우드"),
    AI("인공지능")
    ;

    private String name;

    MemberCourse(String name) {
        this.name = name;
    }
}
