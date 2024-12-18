package com.ktb10.munggaebe.aicomment.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateAiCommentRes {

    @JsonProperty("response")
    private String response;

    public CreateAiCommentRes(String response) {
        this.response = response;
    }
}
