package com.ktb10.munggaebe.image.domain;

public enum ImageType {
    POST("/posts"),
    MEMBER("/members");

    private final String basePath;

    ImageType(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }
}
