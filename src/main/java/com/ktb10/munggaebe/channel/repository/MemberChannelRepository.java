package com.ktb10.munggaebe.channel.repository;

import com.ktb10.munggaebe.channel.domain.MemberChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChannelRepository extends JpaRepository<MemberChannel, Long> {
}