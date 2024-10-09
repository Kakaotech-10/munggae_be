package com.ktb10.munggaebe.post.repository;

import com.ktb10.munggaebe.post.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
