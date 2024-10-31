package com.ktb10.munggaebe.image.domain;

import com.ktb10.munggaebe.post.domain.Post;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue(value = "post")
public class PostImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
