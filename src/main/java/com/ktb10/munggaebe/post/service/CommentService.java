package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;


    public Page<Comment> getRootComments(long postId, int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt"));

        return commentRepository.findByPostId(postId, pageable);
    }
}
