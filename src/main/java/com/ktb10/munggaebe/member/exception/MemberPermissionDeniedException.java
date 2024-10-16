package com.ktb10.munggaebe.member.exception;

import com.ktb10.munggaebe.member.domain.MemberRole;
import lombok.Getter;

@Getter
public class MemberPermissionDeniedException extends RuntimeException {

    private long id;
    private MemberRole role;

    public MemberPermissionDeniedException(long id, MemberRole role) {
        super("해당 Member는 권한이 없습니다. id : " + id + ", role : " + role);
        this.id = id;
        this.role = role;
    }
}
