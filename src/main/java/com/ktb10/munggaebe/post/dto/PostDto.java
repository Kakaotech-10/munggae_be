package com.ktb10.munggaebe.post.dto;

import com.ktb10.munggaebe.member.dto.MemberDto;
import com.ktb10.munggaebe.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateReq {
        private String title;
        private String content;

        @Builder
        public CreateReq(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public Post toEntity() {
            return Post.builder()
                    .title(this.title)
                    .content(this.content)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateReq {
        private String title;
        private String content;

        @Builder
        public UpdateReq(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    public static class Res {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private MemberDto.Res member;

        public Res(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
            this.member = new MemberDto.Res(post.getMember());
        }
    }
}
