package com.ktb10.munggaebe.post.repository;

import com.ktb10.munggaebe.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCreatedAtBefore(LocalDateTime time, Pageable pageable);

    Page<Post> findByChannelIdAndDeadLineIsNotNull(Long channelId, Pageable pageable);

    Page<Post> findByChannelIdAndCreatedAtBefore(Long channelId, LocalDateTime time, Pageable pageable); //channelId 조건을 포함한 게시글 조회 쿼리
}
