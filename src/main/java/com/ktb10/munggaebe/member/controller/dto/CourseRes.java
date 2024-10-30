package com.ktb10.munggaebe.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(description = "맴버 과정 종류 응답")
@Getter
public class CourseRes {

    @Schema(description = "맴버 과정 종류")
    private List<String> courses;

    public CourseRes(List<String> courses) {
        this.courses = courses;
    }
}
