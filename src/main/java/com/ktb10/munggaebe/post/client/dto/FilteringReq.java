package com.ktb10.munggaebe.post.client.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilteringReq {

    private String text;

    public FilteringReq(String text) {
        this.text = text;
    }
}
