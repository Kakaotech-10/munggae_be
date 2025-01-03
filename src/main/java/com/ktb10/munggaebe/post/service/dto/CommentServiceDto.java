package com.ktb10.munggaebe.post.service.dto;

import lombok.Builder;
import lombok.Getter;

public class CommentServiceDto {

    @Getter
    public static class UpdateReq {
        private Long commentId;
        private String content;

        @Builder
        public UpdateReq(long commentId, String content) {
            this.commentId = commentId;
            this.content = content;
        }
    }
}
