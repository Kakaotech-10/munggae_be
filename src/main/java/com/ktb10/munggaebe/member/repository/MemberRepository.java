package com.ktb10.munggaebe.member.repository;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByKakaoId(Long kakaoId);

    Optional<Member> findByNameEnglish(String nameEnglish);

    List<Member> findAllByRole(MemberRole role); // 매니저 조회 메서드
}
