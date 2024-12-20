package com.ktb10.munggaebe.channel.repository;

import com.ktb10.munggaebe.channel.domain.Channel;
import com.ktb10.munggaebe.channel.domain.MemberChannel;
import com.ktb10.munggaebe.member.domain.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MemberChannelRepository extends JpaRepository<MemberChannel, Long> {
    boolean existsByChannelAndMember(Channel channel, Member member);

    List<MemberChannel> findByChannelId(long channelId);

    @Query("SELECT mc.canPost FROM MemberChannel mc WHERE mc.channel.id = :channelId AND mc.member.id = :memberId")
    Boolean findCanPostByChannelIdAndMemberId(@Param("channelId") Long channelId, @Param("memberId") Long memberId);
}