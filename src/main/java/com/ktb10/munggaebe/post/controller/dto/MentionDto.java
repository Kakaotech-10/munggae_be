package com.ktb10.munggaebe.post.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MentionDto {

    @Schema(description = "멘션 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MentionReq {
        @Schema(description = "이름", example = "yohan.Kim(김요한)")
        private String name;

        public MentionReq(String name) {
            this.name = name;
        }
    }

    @Schema(description = "검색 응답")
    @Getter
    public static class MentionSearchRes {
        @Schema(description = "개수", example = "2")
        private int count;

        @Schema(description = "검색된 이름들", example = "[\"yohan.Kim(김요한)\", \"yozi.Kim(김요지)\"]")
        private List<String> result;

        public MentionSearchRes(int count, List<String> result) {
            this.count = count;
            this.result = result;
        }
    }
}
