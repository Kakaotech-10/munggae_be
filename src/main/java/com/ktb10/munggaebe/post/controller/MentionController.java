package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.service.MentionService;
import com.ktb10.munggaebe.post.service.dto.MemberSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentions")
public class MentionController {

    private final MentionService mentionService;

    //멘션 시, 멘션 대상 받아서 알림 발송

    //이름 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchMemberName(@RequestParam String memberName) {

        List<MemberSearchDto> memberSearchDtos = mentionService.searchMemberName(memberName);

        return ResponseEntity.ok(memberSearchDtos);
    }
}
