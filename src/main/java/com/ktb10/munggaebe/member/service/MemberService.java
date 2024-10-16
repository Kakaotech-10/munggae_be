package com.ktb10.munggaebe.member.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public void joinKakao(Long kakaoId, String nickName) {

        if (memberRepository.findByKakaoId(kakaoId).isEmpty()) {

            Member member = Member.builder()
                    .role(MemberRole.STUDENT)
                    .course("default course")
                    .kakaoId(kakaoId)
                    .name(nickName)
                    .nameEnglish("default English name")
                    .build();
            memberRepository.save(member);
        }
    }
}
