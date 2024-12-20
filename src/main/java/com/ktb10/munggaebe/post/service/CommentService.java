package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.notification.domain.NotificationType;
import com.ktb10.munggaebe.notification.service.NotificationEventPublisher;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.service.dto.CommentServiceDto;
import com.ktb10.munggaebe.post.exception.CommentNotFoundException;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import com.ktb10.munggaebe.post.repository.CommentRepository;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FilteringService filteringService;
    private final NotificationEventPublisher notificationEventPublisher;

    private static final int ROOT_COMMENT_DEPTH = 0;
    private static final int REPLY_DEPTH = 1;

    private static final String COMMENT_NOTIFICATION_MESSAGE = "새로운 댓글이 달렸어요!";

    public Page<Comment> getRootComments(final long postId, final int pageNo, final int pageSize) {

        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt"));

        return commentRepository.findByPostIdAndDepth(postId, 0, pageable);
    }

    public Comment getRootComment(final long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    @Transactional
    public Comment createRootComment(final Comment entity, final long postId) {

        log.info("createRootComment start : postId = {}, content = {}", postId, entity.getContent());

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        final Member member;
        if (entity.getMember() == null) {
            Long currentMemberId = SecurityUtil.getCurrentUserId();
            member = memberRepository.findById(currentMemberId)
                    .orElseThrow(() -> new MemberNotFoundException(currentMemberId));

            sendNotification(currentMemberId, post.getMember().getId(), NotificationType.ADD_ROOT_COMMENT);
        } else {
            member = entity.getMember();
        }

        boolean isCommentClean = isCommentClean(entity.getContent());
        log.info("isCommentClean = {}", isCommentClean);

        final Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .parent(null)
                .content(entity.getContent())
                .depth(ROOT_COMMENT_DEPTH)
                .isClean(isCommentClean)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment createReplyComment(final Comment entity, final long commentId) {

        log.info("createReplyComment start : commentId = {}, content = {}", commentId, entity.getContent());
        Long currentMemberId = SecurityUtil.getCurrentUserId();

        final Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        final Long postId = parent.getPost().getId();
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        final Member member = memberRepository.findById(currentMemberId)
                .orElseThrow(() -> new MemberNotFoundException(currentMemberId));

        sendNotification(currentMemberId, parent.getMember().getId(), NotificationType.ADD_REPLY_COMMENT);

        boolean isCommentClean = isCommentClean(entity.getContent());
        log.info("isCommentClean = {}", isCommentClean);

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .parent(parent)
                .content(entity.getContent())
                .depth(REPLY_DEPTH)
                .isClean(isCommentClean)
                .build();

        return commentRepository.save(comment);
    }



    @Transactional
    public Comment updateComment(final CommentServiceDto.UpdateReq updateReq) {

        log.info("updateComment start : commentId = {}, content = {}", updateReq.getCommentId(), updateReq.getContent());
        final Long commentId = updateReq.getCommentId();
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateAuthorization(comment);

        boolean isCommentClean = isCommentClean(updateReq.getContent());
        log.info("isCommentClean = {}", isCommentClean);

        comment.updateComment(updateReq.getContent(), isCommentClean);

        return comment;
    }

    @Transactional
    public void deleteComment(final long commentId) {

        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateAuthorization(comment);

        comment.deleteComment();
    }

    private void validateAuthorization(Comment comment) {
        log.info("validateAuthorization Comment's memberId");
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.hasRole("STUDENT") && !comment.getMember().getId().equals(currentUserId)) {
            throw new MemberPermissionDeniedException(currentUserId, MemberRole.STUDENT);
        }
    }

    private boolean isCommentClean(String content) {
        return filteringService.isCleanText(content);
    }

    private void sendNotification(Long senderId, Long receiverId, NotificationType type) {
        log.info("sendNotification start : senderId = {}, receiverId = {}, NotificationType = {}", senderId, receiverId, type);
        if (senderId.equals(receiverId)) {
            log.info("senderId equals receiverId");
            return;
        }
        log.info("publish notificationEvent");
        NotificationEvent notificationEvent = notificationEventPublisher.createUniCastingEvent(receiverId, type, COMMENT_NOTIFICATION_MESSAGE);
        notificationEventPublisher.publishEvent(notificationEvent);
    }
}
