package com.ktb10.munggaebe.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @Column(name = "course", nullable = false)
    private String course;

    @Column(name = "member_name", nullable = false)
    private String name;

    @Column(name = "member_name_english", nullable = false)
    private String nameEnglish;

    @Column(name = "kakao_id", nullable = false)
    private Long kakaoId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Member(Long id, MemberRole role, String course, String name, String nameEnglish, Long kakaoId) {
        this.id = id;
        this.role = role;
        this.course = course;
        this.name = name;
        this.nameEnglish = nameEnglish;
        this.kakaoId = kakaoId;
    }
}
