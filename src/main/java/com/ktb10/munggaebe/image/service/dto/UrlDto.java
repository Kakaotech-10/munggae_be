package com.ktb10.munggaebe.image.service.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UrlDto {
    private String fileName;
    private String url;

    public UrlDto(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
