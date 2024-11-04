package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.service.ImageService;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import com.ktb10.munggaebe.post.repository.PostRepository;
import com.ktb10.munggaebe.post.service.dto.PostServiceDto;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

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

    public List<PostServiceDto.PresignedUrlRes> getPresignedUrl(final long postId, final List<String> fileNames) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuthorization(post);

        return fileNames.stream()
                .map(fileName -> PostServiceDto.PresignedUrlRes.builder()
                                .fileName(fileName)
                                .url(imageService.getPresignedUrl(fileName, postId, ImageType.POST))
                                .build())
                .toList();
    }

    @Transactional
    public List<PostImage> saveImages(long postId, List<UrlDto> urls) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuthorization(post);

        return imageService.savePostImages(post, urls);
    }

    public List<String> getPostImageUrls(long postId) {

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }
        return imageService.getPostImageUrls(postId);
    }
}
