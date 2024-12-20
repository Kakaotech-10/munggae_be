package com.ktb10.munggaebe.channel.repository;

import com.ktb10.munggaebe.channel.domain.Channel;
import com.ktb10.munggaebe.channel.domain.MemberChannel;
import com.ktb10.munggaebe.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberChannelRepository extends JpaRepository<MemberChannel, Long> {
    boolean existsByChannelAndMember(Channel channel, Member member);

    List<MemberChannel> findByChannelId(long channelId);
}