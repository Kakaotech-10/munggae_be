package com.ktb10.munggaebe.post.domain;

import com.ktb10.munggaebe.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> replies = new ArrayList<>();

    @Column(name = "post_title", nullable = false)
    private String title;

    @Lob
    @Column(name = "post_content", nullable = false, length = 256)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Post(Long id, Member member, String title, String content) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
