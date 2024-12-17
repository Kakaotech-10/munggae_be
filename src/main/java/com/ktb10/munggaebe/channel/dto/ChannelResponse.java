package com.ktb10.munggaebe.channel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채널 생성 응답")
public class ChannelResponse {
    @Schema(description = "채널 ID", example = "123")
    private Long id;

    @Schema(description = "채널 이름", example = "스터디 그룹 A")
    private String name;

    @Schema(description = "해당 채널에 게시물 업로드 가능 여부", example = "true")
    private Boolean canPost;
}