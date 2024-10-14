package com.ktb10.munggaebe.post.dto;

import com.ktb10.munggaebe.member.dto.MemberDto;
import com.ktb10.munggaebe.post.domain.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateReq {
        private String content;

        public CreateReq(String content) {
            this.content = content;
        }

        public Comment toEntity() {
            return Comment.builder()
                    .content(this.content)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateReq {
        private String content;

        public UpdateReq(String content) {
            this.content = content;
        }
    }

    @Getter
    public static class Res {
        private Long id;
        private Long postId;
        private Long parentId;
        private Integer depth;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private MemberDto.Res member;

        public Res(Comment comment) {
            this.id = comment.getId();
            this.postId = comment.getPost().getId();
            this.parentId = comment.getParent() == null ? null : comment.getParent().getId();
            this.depth = comment.getDepth();
            this.content = comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.member = new MemberDto.Res(comment.getMember());
        }
    }

    @Getter
    public static class ResWithReplies {
        private Long id;
        private Long postId;
        private Long parentId;
        private Integer depth;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private MemberDto.Res member;
        private List<Res> replies;

        public ResWithReplies(Comment comment) {
            this.id = comment.getId();
            this.postId = comment.getPost().getId();
            this.parentId = comment.getParent() == null ? null : comment.getParent().getId();
            this.depth = comment.getDepth();
            this.content = comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.member = new MemberDto.Res(comment.getMember());
            this.replies = comment.getReplies().stream()
                    .map(Res::new)
                    .collect(Collectors.toList());
        }
    }
}
