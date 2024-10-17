package com.ktb10.munggaebe.post.repository;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("postId와 depth로 댓글 페이지 조회 - depth 0")
    void findByPostIdAndDepth_ShouldReturnPagedComments_WhenCommentsExist_RootComment() {
        // given
        Member member = memberRepository.save(Member.builder().name("testMember").nameEnglish("testEngName").course("fullstack")
                .role(MemberRole.STUDENT).kakaoId(1234567890L).build());
        Post post = postRepository.save(Post.builder().title("Test Post").content("This is a test post").member(member).build());

        Comment comment1 = Comment.builder().post(post).member(member).content("Root comment 1").depth(0).build();
        Comment comment2 = Comment.builder().post(post).member(member).content("Root comment 2").depth(0).build();
        Comment reply1 = Comment.builder().post(post).member(member).content("Reply 1").depth(1).build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(reply1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());

        // when
        Page<Comment> result = commentRepository.findByPostIdAndDepth(post.getId(), 0, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);  // root comment 2개만 조회
        assertThat(result.getContent().get(0).getContent()).isEqualTo("Root comment 1");
        assertThat(result.getContent().get(1).getContent()).isEqualTo("Root comment 2");
    }

    @Test
    @DisplayName("postId와 depth로 댓글 페이지 조회 - depth 1")
    void findByPostIdAndDepth_ShouldReturnPagedComments_WhenCommentsExist_ReplyComoment() {
        // given
        Member member = memberRepository.save(Member.builder().name("testMember").nameEnglish("testEngName").course("fullstack")
                .role(MemberRole.STUDENT).kakaoId(1234567890L).build());
        Post post = postRepository.save(Post.builder().title("Test Post").content("This is a test post").member(member).build());

        Comment comment1 = Comment.builder().post(post).member(member).content("Root comment 1").depth(0).build();
        Comment comment2 = Comment.builder().post(post).member(member).content("Root comment 2").depth(0).build();
        Comment reply1 = Comment.builder().post(post).member(member).content("Reply 1").depth(1).build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(reply1);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());

        // when
        Page<Comment> result = commentRepository.findByPostIdAndDepth(post.getId(), 1, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);  // root comment 2개만 조회
        assertThat(result.getContent().getFirst().getContent()).isEqualTo("Reply 1");
    }

    @Test
    @DisplayName("postId와 depth로 댓글 페이지 조회 - 댓글이 없는 경우 빈 페이지 반환")
    void findByPostIdAndDepth_ShouldReturnEmptyPage_WhenNoCommentsExist() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").ascending());

        // when
        Page<Comment> result = commentRepository.findByPostIdAndDepth(999L, 0, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();
    }
}

