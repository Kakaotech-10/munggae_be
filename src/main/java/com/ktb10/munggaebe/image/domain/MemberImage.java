package com.ktb10.munggaebe.image.domain;

import com.ktb10.munggaebe.member.domain.Member;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value = "member")
public class MemberImage extends Image {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
