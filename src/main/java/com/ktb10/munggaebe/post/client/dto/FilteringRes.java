package com.ktb10.munggaebe.post.client.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FilteringRes {

    private String originText;
    private List<String> filteredLabels;
    private String message;

    public FilteringRes(String originText, List<String> filteredLabels, String message) {
        this.originText = originText;
        this.filteredLabels = filteredLabels;
        this.message = message;
    }
}
