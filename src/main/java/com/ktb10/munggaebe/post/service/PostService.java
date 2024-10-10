package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPost(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 게시물을 찾을 수 없습니다."));
    }

    public Post createPost(Post post, long memberId) {

        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("id에 해당하는 맴버를 찾을 수 없습니다."));

        Post postWithMember = Post.builder()
                .member(findMember)
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postRepository.save(postWithMember);
    }
}
