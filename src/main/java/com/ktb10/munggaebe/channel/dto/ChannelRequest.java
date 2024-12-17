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
@Schema(description = "채널 생성 요청")
public class ChannelRequest {
    @Schema(description = "채널 이름", example = "스터디 그룹 A")
    private String name;

    @Schema(description = "게시글 작성 권한", example = "true")
    private Boolean canPost;
}