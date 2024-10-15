package com.ktb10.munggaebe.member.dto;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class MemberDto {

    @Schema(description = "맴버 응답")
    @Getter
    public static class MemberRes {
        @Schema(description = "회원 ID", example = "1")
        private Long id;

        @Schema(description = "회원 역할", example = "STUDENT")
        private MemberRole role;

        @Schema(description = "회원의 과정", example = "풀스택")
        private String course;

        @Schema(description = "회원 이름", example = "홍길동")
        private String name;

        @Schema(description = "회원의 영어 이름", example = "Gildong.Hong")
        private String nameEnglish;

        public MemberRes(Member member) {
            this.id = member.getId();
            this.role = member.getRole();
            this.course = member.getCourse();
            this.name = member.getName();
            this.nameEnglish = member.getNameEnglish();
        }
    }
}
