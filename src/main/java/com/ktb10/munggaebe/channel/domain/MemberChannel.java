package com.ktb10.munggaebe.channel.domain;

import com.ktb10.munggaebe.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "can_post", nullable = false)
    private boolean canPost; //학생 포스팅 가능 여부

    @Builder
    public MemberChannel(Channel channel, Member member, boolean canPost) {
        this.channel = channel;
        this.member = member;
        this.canPost = canPost;
    }

}

