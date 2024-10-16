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
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", updatable = false)
    private Comment parent;

    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "depth", nullable = false)
    private Integer depth;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parent")
    List<Comment> replies = new ArrayList<>();

    @Builder
    public Comment(Long id, Post post, Member member, Comment parent, String content, Integer depth) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.parent = parent;
        this.content = content;
        this.depth = depth;
        this.isDeleted = false;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    public void deleteComment() {
        this.isDeleted = true;
    }
}
