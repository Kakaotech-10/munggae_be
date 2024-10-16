package com.ktb10.munggaebe.member.domain;

import lombok.Getter;

@Getter
public enum MemberRole {

    MANAGER("매니저"),
    STUDENT("힉셍")
    ;

    private String name;

    MemberRole(String name) {
        this.name = name;
    }
}
