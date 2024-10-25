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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    private void setupSecurityContextWithRole(Long userId, String role) {
        Member member = Member.builder().id(userId).role(MemberRole.valueOf(role)).build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member, null, Collections.singleton(() -> "ROLE_" + role)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @BeforeEach
    void setupSecurityContextWithStudent() {
        setupSecurityContextWithRole(1L, "STUDENT");
    }

    @Test
    @DisplayName("댓글이 존재하는 경우 root 댓글을 성공적으로 반환한다.")
    void getRootComment_ShouldReturnComment_WhenCommentExists() {
        // given
        long commentId = 1L;
        Comment comment = Comment.builder()
                .id(commentId)
                .content("Sample content")
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        Comment result = commentService.getRootComment(commentId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(commentId);
    }

    @Test
    @DisplayName("댓글이 존재하지 않는 경우 예외가 발생한다.")
    void getRootComment_ShouldThrowException_WhenCommentNotFound() {
        // given
        long commentId = 1L;

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.getRootComment(commentId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("루트 댓글을 성공적으로 생성한다.")
    void createRootComment_ShouldCreateComment_WhenValidInput() {
        // given
        long postId = 1L;
        long memberId = 2L;
        Comment entity = Comment.builder().content("New root comment").build();
        Post post = Post.builder().id(postId).build();
        Member member = Member.builder().id(memberId).build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment result = commentService.createRootComment(entity, postId, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("New root comment");
    }

    @Test
    @DisplayName("게시물이 존재하지 않는 경우 root 댓글 생성 시 예외가 발생한다.")
    void createRootComment_ShouldThrowException_WhenPostNotFound() {
        // given
        long postId = 1L;
        long memberId = 2L;
        Comment entity = Comment.builder().content("New root comment").build();

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createRootComment(entity, postId, memberId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("회원이 존재하지 않는 경우 root 댓글 생성 시 예외가 발생한다.")
    void createRootComment_ShouldThrowException_WhenMemberNotFound() {
        // given
        long postId = 1L;
        long memberId = 2L;
        Comment entity = Comment.builder().content("New root comment").build();
        given(postRepository.findById(postId)).willReturn(Optional.of(Post.builder().id(postId).build()));
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createRootComment(entity, postId, memberId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("답글을 성공적으로 생성한다.")
    void createReplyComment_ShouldCreateComment_WhenValidInput() {
        // given
        long commentId = 1L;
        long memberId = 2L;

        Post post = Post.builder().id(1L).build();
        Comment parentComment = Comment.builder().id(commentId).post(post).build();
        Comment entity = Comment.builder().content("New reply comment").build();
        Member member = Member.builder().id(memberId).build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(parentComment));
        given(postRepository.findById(post.getId())).willReturn(Optional.of(post));
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(commentRepository.save(any(Comment.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment result = commentService.createReplyComment(entity, commentId, memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("New reply comment");
    }

    @Test
    @DisplayName("부모 댓글이 존재하지 않는 경우 답글 생성 시 예외가 발생한다.")
    void createReplyComment_ShouldThrowException_WhenParentCommentNotFound() {
        // given
        long commentId = 1L;
        long memberId = 2L;
        Comment entity = Comment.builder().content("New reply comment").build();

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createReplyComment(entity, commentId, memberId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("댓글이 존재하고, 권한이 있을 경우 댓글을 성공적으로 업데이트한다.")
    void updateComment_ShouldUpdateComment_WhenAuthorized() {
        // given
        long commentId = 1L;
        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Comment comment = Comment.builder()
                .id(commentId)
                .member(member)
                .content("Original content")
                .build();

        CommentServiceDto.UpdateReq updateReq = new CommentServiceDto.UpdateReq(commentId, "Updated content");

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        Comment result = commentService.updateComment(updateReq);

        // then
        assertThat(result.getContent()).isEqualTo("Updated content");
        verify(commentRepository).findById(commentId);
    }

    @Test
    @DisplayName("댓글이 존재하지 않을 경우 예외가 발생한다.")
    void updateComment_ShouldThrowException_WhenCommentNotFound() {
        // given
        long commentId = 1L;
        CommentServiceDto.UpdateReq updateReq = new CommentServiceDto.UpdateReq(commentId, "Updated content");

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(updateReq))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("학생이 자신이 작성하지 않은 댓글을 수정하려고 할 때 예외가 발생한다.")
    void updateComment_ShouldThrowException_WhenUnauthorized() {
        // given
        long commentId = 1L;
        Member otherMember = Member.builder().id(2L).role(MemberRole.STUDENT).build();
        Comment comment = Comment.builder()
                .id(commentId)
                .member(otherMember)
                .content("Original content")
                .build();

        CommentServiceDto.UpdateReq updateReq = new CommentServiceDto.UpdateReq(commentId, "Updated content");

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.updateComment(updateReq))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }

    @Test
    @DisplayName("매니저가 다른 사용자의 댓글을 수정할 수 있다.")
    void updateComment_ShouldUpdateComment_WhenRoleManager() {
        // given
        long commentId = 1L;
        setupSecurityContextWithRole(2L, "MANAGER");

        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Comment comment = Comment.builder()
                .id(commentId)
                .member(member)
                .content("Original content")
                .build();

        CommentServiceDto.UpdateReq updateReq = new CommentServiceDto.UpdateReq(commentId, "Updated content");

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        Comment result = commentService.updateComment(updateReq);

        // then
        assertThat(result.getContent()).isEqualTo("Updated content");
    }

    @Test
    @DisplayName("댓글이 존재하고, 권한이 있을 경우 댓글을 성공적으로 삭제한다.")
    void deleteComment_ShouldDeleteComment_WhenAuthorized() {
        // given
        long commentId = 1L;

        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Comment comment = Comment.builder()
                .id(commentId)
                .member(member)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(commentId);

        // then
        assertThat(comment.isDeleted()).isTrue();
        verify(commentRepository).findById(commentId);
    }

    @Test
    @DisplayName("댓글이 존재하지 않을 경우 예외가 발생한다.")
    void deleteComment_ShouldThrowException_WhenCommentNotFound() {
        // given
        long commentId = 1L;

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(commentId))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("학생이 자신이 작성하지 않은 댓글을 삭제하려고 할 때 예외가 발생한다.")
    void deleteComment_ShouldThrowException_WhenUnauthorized() {
        // given
        long commentId = 1L;

        Member otherMember = Member.builder().id(2L).role(MemberRole.STUDENT).build();
        Comment comment = Comment.builder()
                .id(commentId)
                .member(otherMember)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(commentId))
                .isInstanceOf(MemberPermissionDeniedException.class);
    }

    @Test
    @DisplayName("매니저가 다른 사용자의 댓글을 삭제할 수 있다.")
    void deleteComment_ShouldDeleteComment_WhenRoleManager() {
        // given
        long commentId = 1L;
        setupSecurityContextWithRole(2L, "MANAGER");

        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        Comment comment = Comment.builder()
                .id(commentId)
                .member(member)
                .build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        commentService.deleteComment(commentId);

        // then
        assertThat(comment.isDeleted()).isTrue();
        verify(commentRepository).findById(commentId);
    }
}
