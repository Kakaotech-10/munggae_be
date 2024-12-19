package com.ktb10.munggaebe.post.repository;


import com.ktb10.munggaebe.channel.domain.Channel;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberCourse;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Post;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("channelId와 createdAt 조건에 맞는 게시글을 조회한다")
    void findByChannelIdAndCreatedAtBefore_ShouldReturnResults() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        Member member = memberRepository.save(Member.builder().name("testMember").nameEnglish("testEngName").course(MemberCourse.FULLSTACK)
                .role(MemberRole.STUDENT).kakaoId(1234567890L).build());

        Channel channel = entityManager.persist(new Channel("Test Channel"));

        Post post1 = entityManager.persist(Post.builder()
                .title("Test Post")
                .content("Content 1")
                .channel(channel)
                .member(member)
                .createdAt(now.minusDays(1))
                .build());

        Post post2 = entityManager.persist(Post.builder()
                .title("Test Post2")
                .channel(channel)
                .member(member)
                .content("Content 2")
                .createdAt(now.minusDays(2))
                .build());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Post> result = postRepository.findByChannelIdAndCreatedAtBefore(channel.getId(), now, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).contains(post1, post2);
    }


    @Test
    @DisplayName("createdAt 조건에 맞는 모든 게시글을 조회한다")
    void findByCreatedAtBefore_ShouldReturnResults() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        Member member = memberRepository.save(Member.builder().name("testMember").nameEnglish("testEngName").course(MemberCourse.FULLSTACK)
                .role(MemberRole.STUDENT).kakaoId(1234567890L).build());

        Post post1 = entityManager.persist(Post.builder()
                .title("Test Post")
                .content("Content 1")
                .member(member)
                .createdAt(now.minusDays(1))
                .build());

        Post post2 = entityManager.persist(Post.builder()
                .title("Test Post2")
                .member(member)
                .content("Content 2")
                .createdAt(now.minusDays(2))
                .build());

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Post> result = postRepository.findByCreatedAtBefore(now, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).contains(post1, post2);
    }
}

