package com.ktb10.munggaebe.channel.service;

import com.ktb10.munggaebe.channel.domain.MemberChannel;
import com.ktb10.munggaebe.channel.dto.ChannelRequest;
import com.ktb10.munggaebe.channel.dto.ChannelResponse;
import com.ktb10.munggaebe.channel.repository.MemberChannelRepository;
import com.ktb10.munggaebe.member.controller.dto.MemberDto;
import com.ktb10.munggaebe.channel.repository.ChannelRepository;
import com.ktb10.munggaebe.channel.domain.Channel;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final MemberChannelRepository memberChannelRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ChannelResponse> getChannels() {
        Long currentMemberId = SecurityUtil.getCurrentUserId();

        //매니저 권한인 경우 모든 채널 조회
        if (SecurityUtil.hasRole("MANAGER")) {
            List<Channel> allChannels = channelRepository.findAll();
            return allChannels.stream()
                    .map(channel -> new ChannelResponse(
                            channel.getId(),
                            channel.getName(),
                            true //모든 채널에 canPost를 true로 설정
                    ))
                    .collect(Collectors.toList());
        }

        //학생은 자신이 속한 채널만 조회
        Member currentMember = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return currentMember.getMemberChannels().stream()
                .map(memberChannel -> {
                    Channel channel = memberChannel.getChannel();
                    return new ChannelResponse(
                            channel.getId(),
                            channel.getName(),
                            memberChannel.isCanPost()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ChannelResponse createChannel(ChannelRequest channelRequest) {
        Channel channel = channelRepository.save(new Channel(channelRequest.getName()));

        Long currentMemberId = SecurityUtil.getCurrentUserId();
        Member creator = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        boolean canPost = channelRequest.getCanPost() != null ? channelRequest.getCanPost() : true;

        memberChannelRepository.save(new MemberChannel(channel, creator, canPost));

        return new ChannelResponse(channel.getId(), channel.getName(), canPost);
    }

    @Transactional
    public MemberDto.ChannelMemberResponse addMembers(Long channelId, List<Long> memberIds) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        List<MemberDto.MemberRes> addedMembers = memberIds.stream().map(memberId -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            if (memberChannelRepository.existsByChannelAndMember(channel, member)) {
                throw new RuntimeException("Member with ID " + memberId + " is already in the channel");
            }

            MemberChannel memberChannel = MemberChannel.builder()
                    .channel(channel)
                    .member(member)
                    .canPost(false)
                    .build();
            memberChannelRepository.save(memberChannel);

            return new MemberDto.MemberRes(member);
        }).collect(Collectors.toList());

        return new MemberDto.ChannelMemberResponse(channelId, addedMembers);
    }
}
