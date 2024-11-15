package com.ktb10.munggaebe.image.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ImageCdnPathDto {

    private Long imageId;
    private String fileName;
    private String path;

    @Builder
    public ImageCdnPathDto(Long imageId, String fileName, String path) {
        this.imageId = imageId;
        this.fileName = fileName;
        this.path = path;
    }
}
