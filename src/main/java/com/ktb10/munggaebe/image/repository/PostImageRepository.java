package com.ktb10.munggaebe.image.repository;

import com.ktb10.munggaebe.image.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Query("SELECT pi.s3ImagePath FROM PostImage pi WHERE pi.post.id = :postId")
    List<String> findS3ImagePathsByPostId(@Param("postId") Long postId);

    List<PostImage> findAllByPostId(long postId);
}
