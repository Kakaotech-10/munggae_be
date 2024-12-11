package com.ktb10.munggaebe.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.image.domain.Image;
import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.MemberImage;
import com.ktb10.munggaebe.image.service.ImageService;
import com.ktb10.munggaebe.image.service.dto.ImageCdnPathDto;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final ElasticsearchMemberService elasticsearchMemberService;
    private final ObjectMapper objectMapper;


    @Transactional
    public MemberServiceDto.JoinOrLoginKakaoRes loginOrJoinKakao(Long kakaoId, String nickName) {

        log.info("loginOrJoinKakao start");
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

            Member savedMember = memberRepository.save(member);

            return MemberServiceDto.JoinOrLoginKakaoRes.builder()
                    .isMemberJoin(true)
                    .member(savedMember)
                    .build();
        }

        log.info("kakaoId로 찾을 수 있는 맴버 로그인");
        return MemberServiceDto.JoinOrLoginKakaoRes.builder()
                .isMemberJoin(false)
                .member(findMember.get())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String kakaoId) throws UsernameNotFoundException {
        return memberRepository.findByKakaoId(Long.parseLong(kakaoId))
                .orElseThrow(() -> new UsernameNotFoundException("kakao id에 해당하는 유저를 찾을 수 없습니다."));
    }

    @Transactional
    public Member updateMember(final MemberServiceDto.UpdateReq updateReq) {

        final Long memberId = updateReq.getMemberId();
        final Member member = findMemberById(memberId);

        validateAuthorization(memberId);

        member.updateMember(updateReq.getName(), updateReq.getNameEnglish(), updateReq.getCourse());
        elasticsearchMemberService.addMember(updateReq.getName(), updateReq.getNameEnglish());

        return member;
    }

    private void validateAuthorization(final long memberId) {
        log.info("validateAuthorization memberId");
        final Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId != memberId) {
            throw new MemberPermissionDeniedException(currentUserId);
        }
    }

    public List<String> getCourses() {
        return Arrays.stream(MemberCourse.values())
                .filter(course -> !course.equals(MemberCourse.DEFAULT))
                .map(MemberCourse::getName)
                .toList();
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    public String getPresignedUrl(final long memberId, final String fileName) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }

        validateAuthorization(memberId);

        return imageService.getPresignedUrl(fileName, memberId, ImageType.MEMBER);
    }

    @Transactional
    public MemberImage saveImage(long memberId, UrlDto urls) {

        Member member = findMemberById(memberId);

        validateAuthorization(memberId);

        return imageService.saveMemberImage(member, urls);
    }

    public MemberServiceDto.ImageCdnPathRes getMemberImageUrl(long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
        ImageCdnPathDto imageCdnPath = imageService.getMemberImageUrl(memberId);

        return objectMapper.convertValue(imageCdnPath, MemberServiceDto.ImageCdnPathRes.class);
    }

    @Transactional
    public MemberImage updateImage(long memberId, long imageId, UrlDto imageInfo) {

        log.info("updateImage start : memberId = {}, imageId = {}", memberId, imageId);
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
        validateAuthorization(memberId);

        Image updatedImage = imageService.updateImage(imageId, imageInfo);

        if (updatedImage instanceof MemberImage memberImage) {
            return memberImage;
        }
        throw new IllegalStateException("해당 이미지가 MemberImage 타입이 아닙니다.");
    }

    public Member findMemberByFullName(String name) {

        String nameEnglish = name.split("\\(")[0];
        return memberRepository.findByNameEnglish(nameEnglish)
                .orElseThrow(() -> new NoSuchElementException("해당하는 맴버를 찾을 수 없습니다. name = " + name));
    }
}
