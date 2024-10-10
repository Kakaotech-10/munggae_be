package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPost(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 게시물을 찾을 수 없습니다."));
    }
}
