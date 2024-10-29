package com.ktb10.munggaebe.member.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Member API", description = "맴버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/members/{memberId}")
    @Operation(summary = "맴버 수정", description = "맴버 이름, 영어이름, 과정을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "맴버 수정 성공")
    public ResponseEntity<MemberDto.MemberRes> updateMember(@PathVariable final long memberId,
                                          @RequestBody final MemberDto.MemberUpdateReq request) {
        final MemberServiceDto.UpdateReq updateReq = toServiceDto(memberId, request);
        final Member updatedMember = memberService.updateMember(updateReq);

        return ResponseEntity.ok(new MemberDto.MemberRes(updatedMember));
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
