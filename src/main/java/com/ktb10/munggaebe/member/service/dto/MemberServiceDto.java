package com.ktb10.munggaebe.member.service.dto;

import com.ktb10.munggaebe.member.domain.MemberCourse;
import lombok.Builder;
import lombok.Getter;

public class MemberServiceDto {

    @Getter
    public static class UpdateReq {
        private Long memberId;
        private String name;
        private String nameEnglish;
        private MemberCourse course;

        @Builder
        public UpdateReq(Long memberId, String name, String nameEnglish, MemberCourse course) {
            this.memberId = memberId;
            this.name = name;
            this.nameEnglish = nameEnglish;
            this.course = course;
        }
    }
}
