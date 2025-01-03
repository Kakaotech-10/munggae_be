package com.ktb10.munggaebe.member.domain;

import com.ktb10.munggaebe.channel.domain.MemberChannel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "course", nullable = false, length = 50)
    private MemberCourse course;

    @Column(name = "member_name", nullable = false, length = 50)
    private String name;

    @Column(name = "member_name_english", nullable = false, length = 50)
    private String nameEnglish;

    @Column(name = "kakao_id", nullable = false)
    private Long kakaoId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberChannel> memberChannels = new ArrayList<>();

    @Builder
    public Member(Long id, MemberRole role, MemberCourse course, String name, String nameEnglish, Long kakaoId) {
        this.id = id;
        this.role = role;
        this.course = course;
        this.name = name;
        this.nameEnglish = nameEnglish;
        this.kakaoId = kakaoId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateMember(String name, String nameEnglish, MemberCourse course) {
        this.name = name;
        this.nameEnglish = nameEnglish;
        this.course = course;
    }
}
