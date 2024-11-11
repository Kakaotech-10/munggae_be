package com.ktb10.munggaebe.member.controller;

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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Member API", description = "맴버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/{memberId}")
    @Operation(summary = "단일 맴버 id로 조회", description = "맴버 id, 이름, 영어이름, 과정, 권한을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "단일 맴버 정보 반환 성공")
    public ResponseEntity<MemberDto.MemberRes> getMember(@PathVariable Long memberId) {

        Member member = memberService.findMemberById(memberId);

        return ResponseEntity.ok(new MemberDto.MemberRes(member));
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

        return ResponseEntity.ok(new MemberDto.MemberRes(updatedMember));
    }

    @PostMapping("/members/{memberId}/images/presigned-url")
    @Operation(summary = "맴버 이미지 사전 서명 url 생성", description = "S3로부터 사전 서명 url을 생성해 반환합니다.")
    @ApiResponse(responseCode = "200", description = "맴버 이미지 사전 서명 url 생성 성공")
    public ResponseEntity<MemberDto.ImagePresignedUrlRes> getPresignedUrl(@PathVariable final long memberId,
                                                                        @RequestBody final MemberDto.ImagePresignedUrlReq request) {
        String url = memberService.getPresignedUrl(memberId, request.getFileName());


        return ResponseEntity.ok(
                new MemberDto.ImagePresignedUrlRes(new UrlDto(request.getFileName(), url))
        );
    }

    private static MemberServiceDto.UpdateReq toServiceDto(final long memberId, final MemberDto.MemberUpdateReq request) {
        return MemberServiceDto.UpdateReq.builder()
                .memberId(memberId)
                .name(request.getName())
                .nameEnglish(request.getNameEnglish())
                .course(request.getCourse())
                .build();
    }
}
