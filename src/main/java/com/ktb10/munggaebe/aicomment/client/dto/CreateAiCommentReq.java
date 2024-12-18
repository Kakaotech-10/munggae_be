package com.ktb10.munggaebe.aicomment.client.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateAiCommentReq {

    private String content;

    public CreateAiCommentReq(String content) {
        this.content = content;
    }
}
