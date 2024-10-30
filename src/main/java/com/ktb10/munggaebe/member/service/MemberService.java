package com.ktb10.munggaebe.member.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberCourse;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.member.service.dto.MemberServiceDto;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
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
                    .course(MemberCourse.DEFAULT)
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

    @Transactional
    public Member updateMember(final MemberServiceDto.UpdateReq updateReq) {

        final Long memberId = updateReq.getMemberId();
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        validateAuthorization(memberId);

        member.updateMember(updateReq.getName(), updateReq.getNameEnglish(), updateReq.getCourse());

        return member;
    }

    private void validateAuthorization(final long memberId) {
        final Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId != memberId) {
            throw new MemberPermissionDeniedException(currentUserId);
        }
    }

    public List<String> getCourses() {
        return Arrays.stream(MemberCourse.values())
                .map(MemberCourse::getName)
                .toList();
    }
}
