package com.ktb10.munggaebe.image.domain;

import com.ktb10.munggaebe.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "post")
public class PostImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public PostImage(Long id, String originalName, String storedName, String s3ImagePath, Post post) {
        super(id, originalName, storedName, s3ImagePath);
        this.post = post;
    }
}
