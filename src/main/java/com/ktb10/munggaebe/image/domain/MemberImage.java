package com.ktb10.munggaebe.image.domain;

import com.ktb10.munggaebe.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "member")
public class MemberImage extends Image {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public MemberImage(Long id, String originalName, String storedName, String s3ImagePath, Member member) {
        super(id, originalName, storedName, s3ImagePath);
        this.member = member;
    }
}
