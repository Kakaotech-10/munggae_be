package com.ktb10.munggaebe.member.controller.dto;

import com.ktb10.munggaebe.image.domain.MemberImage;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberCourse;
import com.ktb10.munggaebe.member.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {

    @Schema(description = "맴버 수정 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberUpdateReq {
        @Schema(description = "수정된 맴버 이름", example = "김요한")
        private String name;

        @Schema(description = "수정된 맴버 영어 이름", example = "Yohan Kim")
        private String nameEnglish;

        @Schema(description = "수정된 맴버 과정", example = "FULLSTACK or CLOUD or AI")
        private MemberCourse course;

        @Builder
        public MemberUpdateReq(String name, String nameEnglish, MemberCourse course) {
            this.name = name;
            this.nameEnglish = nameEnglish;
            this.course = course;
        }
    }

    @Schema(description = "맴버 응답")
    @Getter
    public static class MemberRes {
        @Schema(description = "회원 ID", example = "1")
        private Long id;

        @Schema(description = "회원 역할", example = "STUDENT")
        private MemberRole role;

        @Schema(description = "회원의 과정", example = "풀스택")
        private MemberCourse course;

        @Schema(description = "회원 이름", example = "홍길동")
        private String name;

        @Schema(description = "회원의 영어 이름", example = "Gildong.Hong")
        private String nameEnglish;

        @Schema(description = "CDN 이미지 url", example = "{\"imageId\": 1, \"fileName\": \"file1.jpg\", \"path\": \"http://cdn-path/123_file1.jpg\"}")
        private MemberImageCdnPathRes imageUrl;

        public MemberRes(Member member) {
            this.id = member.getId();
            this.role = member.getRole();
            this.course = member.getCourse();
            this.name = member.getName();
            this.nameEnglish = member.getNameEnglish();
        }

        public MemberRes(Member member, MemberImageCdnPathRes imageUrl) {
            this.id = member.getId();
            this.role = member.getRole();
            this.course = member.getCourse();
            this.name = member.getName();
            this.nameEnglish = member.getNameEnglish();
            this.imageUrl = imageUrl;
        }
    }

    @Schema(description = "사전 서명 url 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberImagePresignedUrlReq {

        @Schema(description = "파일 이름 리스트", example = "file1.jpg")
        private String fileName;

        public MemberImagePresignedUrlReq(String fileName) {
            this.fileName = fileName;
        }
    }

    @Schema(description = "사전 서명 url 응답")
    @Getter
    public static class MemberImagePresignedUrlRes {
        @Schema(description = "파일 이름, url")
        private UrlDto urls;

        public MemberImagePresignedUrlRes(UrlDto urls) {
            this.urls = urls;
        }
    }

    @Schema(description = "이미지 저장 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberImageSaveReq {
        @Schema(description = "파일 이름, url")
        private UrlDto urls;

        public MemberImageSaveReq(UrlDto urls) {
            this.urls = urls;
        }
    }

    @Schema(description = "이미지 저장 응답")
    @Getter
    public static class MemberImageRes {
        private Long imageId;
        private Long memberId;
        private String originalName;
        private String storedName;
        private String s3ImagePath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public MemberImageRes(MemberImage memberImage) {
            this.imageId = memberImage.getId();
            this.memberId = memberImage.getMember().getId();
            this.originalName = memberImage.getOriginalName();
            this.storedName = memberImage.getStoredName();
            this.s3ImagePath = memberImage.getS3ImagePath();
            this.createdAt = memberImage.getCreatedAt();
            this.updatedAt = memberImage.getUpdatedAt();
        }
    }

    @Schema(description = "cdn path 응답")
    @Getter
    public static class MemberImageCdnPathRes {
        private Long imageId;
        private String fileName;
        private String path;

        public MemberImageCdnPathRes(Long imageId, String fileName, String path) {
            this.imageId = imageId;
            this.fileName = fileName;
            this.path = path;
        }
    }

    @Schema(description = "이미지 수정 요청")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberImageUpdateReq {
        @Schema(description = "파일 이름, url")
        private UrlDto imageInfo;

        public MemberImageUpdateReq(UrlDto imageInfo) {
            this.imageInfo = imageInfo;
        }
    }

    //채널 기능 추가
    @Schema(description = "채널에 멤버 추가 요청")
    @Getter
    @NoArgsConstructor
    public static class MemberAddReq {
        @Schema(description = "추가할 멤버 ID 리스트", example = "[1, 2, 3]")
        private List<Long> memberIds;

        public MemberAddReq(List<Long> memberIds) {
            this.memberIds = memberIds;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ChannelMemberResponse {
        @Schema(description = "채널 ID", example = "1")
        private Long channelId;

        @Schema(description = "추가된 멤버 리스트")
        private List<MemberRes> members;
    }

}
