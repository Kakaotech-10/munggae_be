package com.ktb10.munggaebe.post.service.dto;

import lombok.Builder;
import lombok.Getter;

public class PostServiceDto {

    @Getter
    public static class UpdateReq {
        private Long postId;
        private String title;
        private String content;

        @Builder
        public UpdateReq(long postId, String title, String content) {
            this.postId = postId;
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    public static class PresignedUrlRes {
        private String fileName;
        private String url;

        @Builder
        public PresignedUrlRes(String fileName, String url) {
            this.fileName = fileName;
            this.url = url;
        }
    }
}
