package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.controller.dto.MentionDto;
import com.ktb10.munggaebe.post.service.MentionService;
import com.ktb10.munggaebe.post.service.dto.MemberSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentions")
public class MentionController {

    private final MentionService mentionService;

    @PostMapping
    @Operation(summary = "사용자 멘션", description = "멘션 시, 멘션 대상 받아서 알림 발송합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 멘션 성공")
    public ResponseEntity<?> mentioned(@RequestBody MentionDto mentionDto) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(summary = "이름 검색", description = "사용자의 입력을 바탕으로 멘션할 이름을 검색합니다.")
    @ApiResponse(responseCode = "200", description = "이름 검색 성공")
    public ResponseEntity<?> searchMemberName(@RequestParam String memberName) {

        //리턴값 한글이름, 영어이름 합쳐서 보내도록 수정
        List<MemberSearchDto> memberSearchDtos = mentionService.searchMemberName(memberName);

        return ResponseEntity.ok(memberSearchDtos);
    }
}
