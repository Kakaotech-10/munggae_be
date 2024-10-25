package com.ktb10.munggaebe.member.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Transactional
    public Member joinKakao(Long kakaoId, String nickName) {

        log.info("joinKakao start");
        Optional<Member> findMember = memberRepository.findByKakaoId(kakaoId);

        if (findMember.isEmpty()) {
            log.info("kakaoId로 찾을 수 없는 맴버 회원가입");
            Member member = Member.builder()
                    .role(MemberRole.STUDENT)
                    .course("default course")
                    .kakaoId(kakaoId)
                    .name(nickName)
                    .nameEnglish("default English name")
                    .build();
            return memberRepository.save(member);
        }
        log.info("kakaoId로 찾을 수 있는 맴버 로그인");
        return findMember.get();
    }

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {
        return memberRepository.findByKakaoId(Long.parseLong(kakaoId))
                .orElseThrow(() -> new UsernameNotFoundException("kakao id에 해당하는 유저를 찾을 수 없습니다."));
    }
}
