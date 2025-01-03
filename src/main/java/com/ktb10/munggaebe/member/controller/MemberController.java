package com.ktb10.munggaebe.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.image.domain.MemberImage;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.controller.dto.CourseRes;
import com.ktb10.munggaebe.member.controller.dto.MemberDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.service.MemberService;
import com.ktb10.munggaebe.member.service.dto.MemberServiceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Member API", description = "맴버 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @GetMapping("/members")
    @Operation(summary = "채널에 없는 Student 맴버 목록 조회", description = "채널에 없는 Student 맴버 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "채널에 없는 Student 맴버 목록 조회 성공")
    public ResponseEntity<MemberDto.MembersRes> getStudentMembers(@RequestParam Long channelId) {
        List<Member> members = memberService.getStudentsExcludingChannel(channelId);

        return ResponseEntity.ok(new MemberDto.MembersRes(members));
    }

    @GetMapping("/members/{memberId}")
    @Operation(summary = "단일 맴버 id로 조회", description = "맴버 id, 이름, 영어이름, 과정, 권한을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "단일 맴버 정보 반환 성공")
    public ResponseEntity<MemberDto.MemberRes> getMember(@PathVariable Long memberId) {

        final Member member = memberService.findMemberById(memberId);

        return ResponseEntity.ok(appendCdnPath(member));
    }

    @GetMapping("/members/course")
    @Operation(summary = "과정 종류 반환", description = "과정에 어떤 종류들이 있는지 반환합니다.")
    @ApiResponse(responseCode = "200", description = "과정 종류 반환 성공")
    public ResponseEntity<CourseRes> getMemberCourses() {

        final List<String> courses = memberService.getCourses();

        return ResponseEntity.ok(new CourseRes(courses));
    }


    @PatchMapping("/members/{memberId}")
    @Operation(summary = "맴버 수정", description = "맴버 이름, 영어이름, 과정을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "맴버 수정 성공")
    public ResponseEntity<MemberDto.MemberRes> updateMember(@PathVariable final long memberId,
                                          @RequestBody final MemberDto.MemberUpdateReq request) {
        final MemberServiceDto.UpdateReq updateReq = toServiceDto(memberId, request);
        final Member updatedMember = memberService.updateMember(updateReq);

        return ResponseEntity.ok(appendCdnPath(updatedMember));
    }

    @PostMapping("/members/{memberId}/images/presigned-url")
    @Operation(summary = "맴버 이미지 사전 서명 url 생성", description = "S3로부터 사전 서명 url을 생성해 반환합니다.")
    @ApiResponse(responseCode = "200", description = "맴버 이미지 사전 서명 url 생성 성공")
    public ResponseEntity<MemberDto.MemberImagePresignedUrlRes> getPresignedUrl(@PathVariable final long memberId,
                                                                                @RequestBody final MemberDto.MemberImagePresignedUrlReq request) {
        final String url = memberService.getPresignedUrl(memberId, request.getFileName());

        return ResponseEntity.ok(
                new MemberDto.MemberImagePresignedUrlRes(new UrlDto(request.getFileName(), url))
        );
    }

    @PostMapping("/members/{memberId}/images")
    @Operation(summary = "맴버 이미지 저장", description = "맴버 이미지 이름과 s3 url을 저장합니다.")
    @ApiResponse(responseCode = "201", description = "맴버 이미지 저장 성공")
    public ResponseEntity<MemberDto.MemberImageRes> saveMemberImage(@PathVariable final long memberId,
                                                                    @RequestBody final MemberDto.MemberImageSaveReq request) {

        final MemberImage memberImage = memberService.saveImage(memberId, request.getUrls());

        return ResponseEntity.created(URI.create("/api/v1/members/" + memberId))
                .body(new MemberDto.MemberImageRes(memberImage));
    }

    @PutMapping("/members/{memberId}/images/{imageId}")
    @Operation(summary = "맴버 이미지 수정", description = "주어진 이미지 id를 통해 이미지를 수정합니다")
    @ApiResponse(responseCode = "200", description = "맴버 이미지 수정 성공")
    public ResponseEntity<MemberDto.MemberImageRes> updatePostImage(@PathVariable final long memberId,
                                                                    @PathVariable final long imageId,
                                                                    @RequestBody final MemberDto.MemberImageUpdateReq request) {

        final MemberImage memberImage = memberService.updateImage(memberId, imageId, request.getImageInfo());

        return ResponseEntity.ok(new MemberDto.MemberImageRes(memberImage));
    }

    private static MemberServiceDto.UpdateReq toServiceDto(final long memberId, final MemberDto.MemberUpdateReq request) {
        return MemberServiceDto.UpdateReq.builder()
                .memberId(memberId)
                .name(request.getName())
                .nameEnglish(request.getNameEnglish())
                .course(request.getCourse())
                .build();
    }

    private MemberDto.MemberRes appendCdnPath(final Member member) {
        final MemberServiceDto.ImageCdnPathRes memberImageCdnPath = memberService.getMemberImageUrl(member.getId());
        return new MemberDto.MemberRes(member, objectMapper.convertValue(memberImageCdnPath, MemberDto.MemberImageCdnPathRes.class));
    }

    private List<MemberDto.MemberRes> appendCdnPaths(final List<Member> members) {
        return members.stream()
                .map(this::appendCdnPath)
                .toList();
    }
}
