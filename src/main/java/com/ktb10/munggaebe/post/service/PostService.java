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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPost(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    @Transactional
    public Post createPost(Post post, long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        Post postWithMember = Post.builder()
                .member(member)
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return postRepository.save(postWithMember);
    }

    @Transactional
    public Post updatePost(PostServiceDto.UpdateReq updateReq, long memberId) {

        long postId = updateReq.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuth(memberId, post.getMember().getId());

        post.updatePost(updateReq.getTitle(), updateReq.getContent());

        return post;
    }

    @Transactional
    public void deletePost(long postId, long memberId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuth(memberId, post.getMember().getId());

        postRepository.deleteById(postId);
    }

    private void validateAuth(long memberId, long postMemberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        if (member.getRole() == MemberRole.STUDENT && memberId != postMemberId) {
            throw new MemberPermissionDeniedException(memberId, member.getRole());
        }
    }
}
