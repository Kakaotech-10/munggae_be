package com.ktb10.munggaebe.channel.controller;

import com.ktb10.munggaebe.channel.dto.ChannelRequest;
import com.ktb10.munggaebe.channel.dto.ChannelResponse;
import com.ktb10.munggaebe.channel.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ktb10.munggaebe.member.controller.dto.MemberDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Channel API", description = "채널 관련 API")
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/channels")
    @Operation(summary = "채널 목록 조회", description = "현재 로그인한 사용자가 속한 채널 리스트를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "채널 목록 조회 성공")
    public ResponseEntity<List<ChannelResponse>> getChannels() {
        List<ChannelResponse> channels = channelService.getChannels();
        return ResponseEntity.ok(channels);
    }

    @PostMapping("/channels")
    @Operation(summary = "채널 생성", description = "새로운 채널을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "채널 생성 성공")
    public ResponseEntity<ChannelResponse> createChannel(@RequestBody ChannelRequest channelRequest) {
        ChannelResponse channelResponse = channelService.createChannel(channelRequest);
        return ResponseEntity.status(201).body(channelResponse);
    }

    @PostMapping("/channels/{channelId}/members")
    @Operation(summary = "채널에 멤버 추가", description = "특정 채널에 멤버를 추가합니다.")
    @ApiResponse(responseCode = "202", description = "채널에 멤버 추가 성공")
    public ResponseEntity<MemberDto.ChannelMemberResponse> addMembersToChannel(
            @PathVariable Long channelId,
            @RequestBody MemberDto.MemberAddReq memberAddReq) {
        MemberDto.ChannelMemberResponse response = channelService.addMembers(channelId, memberAddReq.getMemberIds());
        return ResponseEntity.accepted().body(response);
    }
}
