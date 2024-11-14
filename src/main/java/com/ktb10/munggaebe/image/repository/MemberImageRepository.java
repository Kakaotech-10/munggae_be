package com.ktb10.munggaebe.image.repository;

import com.ktb10.munggaebe.image.domain.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    @Query("SELECT mi.s3ImagePath FROM MemberImage mi WHERE mi.member.id = :memberId")
    String findS3ImagePathsByMemberId(@Param("memberId") Long memberId);

    Optional<MemberImage> findByMemberId(long memberId);
}
