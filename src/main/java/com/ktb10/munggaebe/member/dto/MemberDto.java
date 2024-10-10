package com.ktb10.munggaebe.member.dto;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import lombok.Getter;

public class MemberDto {

    @Getter
    public static class Res {
        private Long id;
        private MemberRole role;
        private String course;
        private String name;
        private String nameEnglish;

        public Res(Member member) {
            this.id = member.getId();
            this.role = member.getRole();
            this.course = member.getCourse();
            this.name = member.getName();
            this.nameEnglish = member.getNameEnglish();
        }
    }
}
