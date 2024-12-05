package com.ktb10.munggaebe.channel.domain;

import com.ktb10.munggaebe.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", updatable = false)
    private Long id;

    @Column(name = "channel_name", nullable = false, length = 100)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "member_channel",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members; // 채널에 속한 멤버들

    @Builder
    public Channel(Long id, String name, LocalDateTime createdAt, LocalDateTime updatedAt, List<Member> members) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.members = members;
    }
}
