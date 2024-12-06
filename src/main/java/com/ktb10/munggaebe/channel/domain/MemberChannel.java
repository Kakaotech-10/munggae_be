package com.ktb10.munggaebe.channel.domain;

import com.ktb10.munggaebe.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member_channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_channel_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public MemberChannel(Long id, Channel channel, Member member) {
        this.id = id;
        this.channel = channel;
        this.member = member;
    }

}

