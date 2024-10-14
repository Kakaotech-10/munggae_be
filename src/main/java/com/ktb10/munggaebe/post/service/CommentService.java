package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.dto.CommentServiceDto;
import com.ktb10.munggaebe.post.exception.CommentNotFoundException;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import com.ktb10.munggaebe.post.repository.CommentRepository;
import com.ktb10.munggaebe.post.repository.PostRepository;
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
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private static final int ROOT_COMMENT_DEPTH = 0;

    public Page<Comment> getRootComments(final long postId, final int pageNo, final int pageSize) {

        final Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt"));

        return commentRepository.findByPostIdAndDepth(postId, 0, pageable);
    }

    public Comment getRootComment(final long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    @Transactional
    public Comment createRootComment(final Comment entity, final long postId, final long memberId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        final Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .parent(null)
                .content(entity.getContent())
                .depth(ROOT_COMMENT_DEPTH)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(final CommentServiceDto.UpdateReq updateReq, final long memberId) {

        final Long commentId = updateReq.getCommentId();
        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateAuth(memberId, comment.getMember().getId());

        comment.updateComment(updateReq.getContent());

        return comment;
    }

    @Transactional
    public void deleteComment(final long commentId, final long memberId) {

        final Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        validateAuth(memberId, comment.getMember().getId());

        comment.deleteComment();
    }

    private void validateAuth(final long memberId, final long commentMemberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));

        if (member.getRole() == MemberRole.STUDENT && commentMemberId != memberId) {
            throw new MemberPermissionDeniedException(memberId, member.getRole());
        }
    }
}
