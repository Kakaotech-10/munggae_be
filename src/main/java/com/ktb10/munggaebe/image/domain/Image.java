package com.ktb10.munggaebe.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", updatable = false)
    private Long id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;

    @Column(name = "s3_image_path", nullable = false)
    private String s3ImagePath;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Image(Long id, String originalName, String storedName, String s3ImagePath) {
        this.id = id;
        this.originalName = originalName;
        this.storedName = storedName;
        this.s3ImagePath = s3ImagePath;
    }
}
