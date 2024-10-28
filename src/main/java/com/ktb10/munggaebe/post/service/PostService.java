package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.dto.PostServiceDto;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import com.ktb10.munggaebe.post.repository.PostRepository;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page<Post> getPosts(final int pageNo, final int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        return postRepository.findAll(pageable);
    }

    public Post getPost(final long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    @Transactional
    public Post createPost(final Post post) {

        Long currentMemberId = SecurityUtil.getCurrentUserId();

        final Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException(currentMemberId));

        final Post postWithMember = Post.builder()
                .member(member)
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postRepository.save(postWithMember);
    }

    @Transactional
    public Post updatePost(final PostServiceDto.UpdateReq updateReq) {

        final long postId = updateReq.getPostId();
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuthorization(post);

        post.updatePost(updateReq.getTitle(), updateReq.getContent());

        return post;
    }

    @Transactional
    public void deletePost(final long postId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuthorization(post);
        
        postRepository.deleteById(postId);
    }

    private void validateAuthorization(Post post) {
        Long currentMemberId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.hasRole("STUDENT") && !post.getMember().getId().equals(currentMemberId)) {
            throw new MemberPermissionDeniedException(currentMemberId, MemberRole.STUDENT);
        }
    }
}
