package com.ktb10.munggaebe.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.aicomment.service.AiCommentService;
import com.ktb10.munggaebe.channel.domain.Channel;
import com.ktb10.munggaebe.channel.repository.ChannelRepository;
import com.ktb10.munggaebe.channel.repository.MemberChannelRepository;
import com.ktb10.munggaebe.image.domain.Image;
import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.service.ImageService;
import com.ktb10.munggaebe.image.service.dto.ImageCdnPathDto;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.exception.PermissionException;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import com.ktb10.munggaebe.post.repository.PostRepository;
import com.ktb10.munggaebe.post.service.dto.PostServiceDto;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final FilteringService filteringService;
    private final ObjectMapper objectMapper;
    private final ChannelRepository channelRepository;
    private final AiCommentService aiCommentService;
    private final MemberChannelRepository memberChannelRepository;

    private static final Long ANNOUNCEMENT_CHANNEL_ID = 1L;
    private static final String EDUCATION_CHANNEL_NAME = "학습게시판";
    private static final String EDUCATION_POST_DELIMITER = "*****";


    //channelId 조회, 게시글 조회 (GET /api/v1/posts)
    public Page<Post> getPosts(Long channelId, final int pageNo, final int pageSize) {
        log.info("getPosts start : channelId = {}", channelId);
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());

        if (channelId != null) {
            log.info("channelId is not null");
            Page<Post> results = postRepository.findByChannelId(channelId, pageable);

            List<Post> filteredPosts = results.getContent().stream()
                    .filter(post -> post.getCreatedAt().isBefore(now))
                    .toList();

            log.info("results.size() = {}", results.getContent().size());
            return new PageImpl<>(filteredPosts, pageable, results.getTotalElements());

        }
        return postRepository.findByCreatedAtBefore(now, pageable);
    }

    public Post getPost(final long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    //channelId 조회, 게시글 생성 (POST /api/v1/posts)
    @Transactional
    public Post createPost(Long channelId, final Post post) {
        log.info("createPost start : title = {}, content = {}", post.getTitle(), post.getContent());
        Long currentMemberId = SecurityUtil.getCurrentUserId();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found with id: " + channelId));

        if (!SecurityUtil.hasRole(MemberRole.MANAGER.name())) {
            // 매니저가 아닌 경우만 canPost 권한 확인
            Boolean canPost = memberChannelRepository.findCanPostByChannelIdAndMemberId(channelId, currentMemberId);
            if (canPost == null || !canPost) {
                throw new PermissionException();
            }
        }

        final Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException(currentMemberId));

        boolean isPostClean = isPostClean(post.getTitle(), post.getContent());
        log.info("createPost isPostClean = {}", isPostClean);

        Post postWithMember = Post.builder()
                .member(member)
                .channel(channel) //channelId 추가
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getReservationTime() == null ? LocalDateTime.now() : post.getReservationTime())
                .reservationTime(post.getReservationTime())
                .deadLine(post.getDeadLine())
                .isClean(isPostClean)
                .build();

        Post savedPost = postRepository.save(postWithMember);

        if (savedPost.getChannel().getName().equals(EDUCATION_CHANNEL_NAME) && containsCodeArea(post.getContent())) {
            String codeArea = duplicateCodeArea(post.getContent());
            log.info("codeArea = {}", codeArea);
            aiCommentService.createAiComment(codeArea, savedPost.getId());
        }

        return savedPost;
    }



    @Transactional
    public Post updatePost(final PostServiceDto.UpdateReq updateReq) {

        log.info("updatePost start : id = {}, title = {}, content = {}", updateReq.getPostId(), updateReq.getTitle(), updateReq.getContent());
        final long postId = updateReq.getPostId();
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuthorization(post);

        boolean isPostClean = isPostClean(updateReq.getTitle(), updateReq.getContent());
        log.info("updatePost isPostClean = {}", isPostClean);

        post.updatePost(updateReq.getTitle(), updateReq.getContent(), isPostClean);

        return post;
    }
    @Transactional
    public void deletePost(final long postId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        validateAuthorization(post);

        postRepository.deleteById(postId);
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

    public List<PostServiceDto.ImageCdnPathRes> getPostImageCdnPaths(long postId) {

        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException(postId);
        }

        List<ImageCdnPathDto> postImageUrls = imageService.getPostImageUrls(postId);

        return postImageUrls.stream()
                .map(u -> objectMapper.convertValue(u, PostServiceDto.ImageCdnPathRes.class))
                .toList();
    }

    @Transactional
    public PostImage updateImage(long postId, long imageId, UrlDto imageInfo) {

        log.info("updateImage start : postId = {}, imageId = {}", postId, imageId);
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        validateAuthorization(post);

        Image updatedImage = imageService.updateImage(imageId, imageInfo);

        if (updatedImage instanceof PostImage postImage) {
            return postImage;
        }
        throw new IllegalStateException("해당 이미지가 PostImage 타입이 아닙니다.");
    }

    public Page<Post> getAnnouncementsPostsNearDeadline(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("deadLine"));
        return postRepository.findByChannelIdAndDeadLineIsNotNull(ANNOUNCEMENT_CHANNEL_ID, pageable);
    }

    public String duplicateCodeArea(String content) {
        int index = content.indexOf(EDUCATION_POST_DELIMITER);

        return content.substring(index + EDUCATION_POST_DELIMITER.length());
    }

    public String duplicateContentArea(String content) {
        int index = content.indexOf(EDUCATION_POST_DELIMITER);

        return content.substring(0, index);
    }

    public boolean containsCodeArea(String content) {
        return content.contains(EDUCATION_POST_DELIMITER);
    }

    private void validateAuthorization(Post post) {
        log.info("validateAuthorization Post's memberId");
        Long currentMemberId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.hasRole("STUDENT") && !post.getMember().getId().equals(currentMemberId)) {
            throw new MemberPermissionDeniedException(currentMemberId, MemberRole.STUDENT);
        }
    }

    private boolean isPostClean(String title, String content) {
        boolean isTitleClean = filteringService.isCleanText(title);
        log.info("isTitleClean = {}", isTitleClean);
        boolean isContentClean = filteringService.isCleanText(content);
        log.info("isContentClean = {}", isContentClean);
        return isTitleClean && isContentClean;
    }
}
