package com.ktb10.munggaebe.post.controller.dto;

import com.ktb10.munggaebe.member.controller.dto.MemberDto;
import com.ktb10.munggaebe.post.domain.Comment;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDto {

    private static final String DELETED_CONTENT = "삭제된 댓글입니다.";

    @Schema(description = "댓글 생성 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentCreateReq {
        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.")
        private String content;

        public CommentCreateReq(String content) {
            this.content = content;
        }

        public Comment toEntity() {
            return Comment.builder()
                    .content(this.content)
                    .build();
        }
    }

    @Schema(description = "댓글 수정 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentUpdateReq {
        @Schema(description = "수정된 댓글 내용", example = "수정된 댓글입니다.")
        private String content;

        public CommentUpdateReq(String content) {
            this.content = content;
        }
    }

    @Schema(description = "댓글 응답, 대댓글 미포함")
    @Getter
    public static class CommentRes {
        @Schema(description = "댓글 ID", example = "1")
        private Long id;

        @Schema(description = "게시글 ID", example = "100")
        private Long postId;

        @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "null")
        private Long parentId;

        @Schema(description = "댓글의 깊이 (0은 원댓글, 1 이상은 대댓글)", example = "0")
        private Integer depth;

        @Schema(description = "댓글 삭제 여부", example = "false")
        private boolean isDeleted;

        @Schema(description = "댓글 내용 (삭제된 경우 '삭제된 댓글입니다.')", example = "이것은 댓글입니다.")
        private String content;

        @Schema(description = "댓글 작성 시간", example = "2024-10-15T10:15:30")
        private LocalDateTime createdAt;

        @Schema(description = "댓글 수정 시간", example = "2024-10-15T10:15:30")
        private LocalDateTime updatedAt;

        @Schema(description = "댓글 작성자 정보", implementation = MemberDto.MemberRes.class)
        private MemberDto.MemberRes member;

        public CommentRes(Comment comment) {
            this.id = comment.getId();
            this.postId = comment.getPost().getId();
            this.parentId = comment.getParent() == null ? null : comment.getParent().getId();
            this.depth = comment.getDepth();
            this.isDeleted = comment.isDeleted();
            this.content = comment.isDeleted() ? DELETED_CONTENT : comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.member = new MemberDto.MemberRes(comment.getMember());
        }
    }

    @Schema(description = "댓글 응답, 대댓글 포함")
    @Getter
    public static class CommentResWithReplies {
        @Schema(description = "댓글 ID", example = "1")
        private Long id;

        @Schema(description = "게시글 ID", example = "100")
        private Long postId;

        @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "null")
        private Long parentId;

        @Schema(description = "댓글의 깊이 (0은 원댓글, 1 이상은 대댓글)", example = "0")
        private Integer depth;

        @Schema(description = "댓글 삭제 여부", example = "false")
        private boolean isDeleted;

        @Schema(description = "댓글 내용 (삭제된 경우 '삭제된 댓글입니다.')", example = "이것은 댓글입니다.")
        private String content;

        @Schema(description = "댓글 작성 시간", example = "2024-10-15T10:15:30")
        private LocalDateTime createdAt;

        @Schema(description = "댓글 수정 시간", example = "2024-10-15T10:15:30")
        private LocalDateTime updatedAt;

        @Schema(description = "댓글 작성자 정보", implementation = MemberDto.MemberRes.class)
        private MemberDto.MemberRes member;

        @ArraySchema(schema = @Schema(implementation = CommentRes.class))
        private List<CommentRes> replies;

        public CommentResWithReplies(Comment comment) {
            this.id = comment.getId();
            this.postId = comment.getPost().getId();
            this.parentId = comment.getParent() == null ? null : comment.getParent().getId();
            this.depth = comment.getDepth();
            this.isDeleted = comment.isDeleted();
            this.content = comment.isDeleted() ? DELETED_CONTENT : comment.getContent();
            this.createdAt = comment.getCreatedAt();
            this.updatedAt = comment.getUpdatedAt();
            this.member = new MemberDto.MemberRes(comment.getMember());
            this.replies = comment.getReplies().stream()
                    .map(CommentRes::new)
                    .collect(Collectors.toList());
        }
    }
}
