package com.ktb10.munggaebe.post.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MentionDto {

    @Schema(description = "멘션 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MentionReq {
        @Schema(description = "한글 이름", example = "김요한")
        private String nameKorean;

        @Schema(description = "영어 이름", example = "yohan.Kim")
        private String nameEnglish;

        @Builder
        public MentionReq(String nameKorean, String nameEnglish) {
            this.nameKorean = nameKorean;
            this.nameEnglish = nameEnglish;
        }
    }
}
