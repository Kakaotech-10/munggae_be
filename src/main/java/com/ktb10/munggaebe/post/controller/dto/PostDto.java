package com.ktb10.munggaebe.post.controller.dto;

import com.ktb10.munggaebe.member.controller.dto.MemberDto;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

    @Schema(description = "게시물 생성 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostCreateReq {
        @Schema(description = "게시글 제목", example = "Spring Boot를 활용한 REST API 개발")
        private String title;

        @Schema(description = "게시글 내용", example = "이 글에서는 Spring Boot를 이용한 REST API를 만드는 방법을 다룹니다.")
        private String content;

        @Builder
        public PostCreateReq(String title, String content) {
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

    @Schema(description = "게시물 수정 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostUpdateReq {
        @Schema(description = "수정된 게시글 제목", example = "수정된 REST API 개발 가이드")
        private String title;

        @Schema(description = "수정된 게시글 내용", example = "REST API에 대한 내용을 좀 더 구체적으로 수정하였습니다.")
        private String content;

        @Builder
        public PostUpdateReq(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    @Schema(description = "사전 서명 url 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ImagePresignedUrlReq {

        @Schema(description = "파일 이름 리스트", example = "[\"file1.jpg\", \"file2.png\"]")
        private List<String> fileNames;

        public ImagePresignedUrlReq(List<String> fileNames) {
            this.fileNames = fileNames;
        }
    }

    @Schema(description = "게시물 응답")
    @Getter
    public static class PostRes {
        @Schema(description = "게시글 ID", example = "1")
        private Long id;

        @Schema(description = "게시글 제목", example = "Spring Boot를 활용한 REST API 개발")
        private String title;

        @Schema(description = "게시글 내용", example = "이 글에서는 Spring Boot를 이용한 REST API를 만드는 방법을 다룹니다.")
        private String content;

        @Schema(description = "게시글 작성 시간", example = "2024-10-15T10:15:30")
        private LocalDateTime createdAt;

        @Schema(description = "게시글 수정 시간", example = "2024-10-15T11:15:30")
        private LocalDateTime updatedAt;

        @Schema(description = "게시물 작성자 정보", implementation = MemberDto.MemberRes.class)
        private MemberDto.MemberRes member;

        public PostRes(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt();
            this.updatedAt = post.getUpdatedAt();
            this.member = new MemberDto.MemberRes(post.getMember());
        }
    }

    @Schema(description = "사전 서명 url 응답")
    @Getter
    public static class ImagePresignedUrlRes {
        @Schema(description = "개수", example = "2")
        private int count;

        @Schema(description = "파일 이름, url")
        private List<UrlDto> urls;

        @Builder
        public ImagePresignedUrlRes(int count, List<UrlDto> urls) {
            this.count = count;
            this.urls = urls;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ImageSaveReq {
        private List<UrlDto> urls;

        public ImageSaveReq(List<UrlDto> urls) {
            this.urls = urls;
        }
    }


}
