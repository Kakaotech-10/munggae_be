package com.ktb10.munggaebe.post.dto;

import com.ktb10.munggaebe.member.dto.MemberDto;
import com.ktb10.munggaebe.post.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostDto {

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
