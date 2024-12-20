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

import static com.ktb10.munggaebe.member.domain.MemberRole.MANAGER;

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
                            channel.getName()
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
                            channel.getName()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ChannelResponse createChannel(ChannelRequest channelRequest) {
        if (!SecurityUtil.hasRole("MANAGER")) {
            throw new RuntimeException("Only managers can create channels.");
        }

        Channel channel = channelRepository.save(new Channel(channelRequest.getName()));
        List<Member> managers = memberRepository.findAllByRole(MANAGER);

        // 모든 매니저를 해당 채널에 추가
        managers.forEach(manager -> {
            memberChannelRepository.save(new MemberChannel(channel, manager, true)); // 매니저는 canPost를 항상 true로 설정
        });

        return new ChannelResponse(channel.getId(), channel.getName());
    }

    @Transactional
    public MemberDto.ChannelMemberResponse addMembers(Long channelId, MemberDto.MemberAddReq memberAddReq) {
        if (!SecurityUtil.hasRole("MANAGER")) {
            throw new RuntimeException("Only managers can add members to a channel.");
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        boolean canPost = memberAddReq.getCanPost();

        List<MemberDto.MemberRes> addedMembers = memberAddReq.getMemberIds().stream().map(memberId -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            if (memberChannelRepository.existsByChannelAndMember(channel, member)) {
                throw new RuntimeException("Member with ID " + memberId + " is already in the channel");
            }

            MemberChannel memberChannel = MemberChannel.builder()
                    .channel(channel)
                    .member(member)
                    .canPost(canPost)
                    .build();
            memberChannelRepository.save(memberChannel);

            return new MemberDto.MemberRes(member);
        }).collect(Collectors.toList());

        return new MemberDto.ChannelMemberResponse(canPost, channelId, addedMembers);
    }

}
