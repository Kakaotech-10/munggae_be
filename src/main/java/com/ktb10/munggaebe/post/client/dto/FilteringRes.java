package com.ktb10.munggaebe.post.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class FilteringRes {

    @JsonProperty("origin_text")
    private String originText;

    @JsonProperty("filtered_labels")
    private List<String> filteredLabels;

    @JsonProperty("message")
    private String message;

    public FilteringRes(String originText, List<String> filteredLabels, String message) {
        this.originText = originText;
        this.filteredLabels = filteredLabels;
        this.message = message;
    }
}
