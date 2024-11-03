package com.ktb10.munggaebe.image.service;

import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.repository.ImageRepository;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    private static final String PREFIX_PATH = "images";

    public String getPresignedUrl(String fileName, Long id, ImageType imageType) {
        log.info("getPresignedUrl start : fileName = {}, id = {}", fileName, id);

        String prefix = getPrefix(id, imageType);
        String storedFileName = convertToStoredFileName(fileName);
        String url = s3Service.generatePresignedUrl(prefix, storedFileName);
        log.info("url = {}", url);

        return url;
    }

    public List<PostImage> savePostImages(Post post, List<UrlDto> urls) {
        log.info("savePostImages start : urls = {}", urls);
        List<PostImage> images = urls.stream()
                .map(u -> PostImage.builder()
                        .post(post)
                        .originalName(u.getFileName())
                        .storedName(getStoredName(u.getUrl()))
                        .s3ImagePath(u.getUrl())
                        .build())
                .toList();

        return imageRepository.saveAll(images);
    }

    private static String getPrefix(Long id, ImageType imageType) {
        return PREFIX_PATH + imageType.getBasePath() + "/" + id;
    }

    private static String convertToStoredFileName(String fileName) {
        return UUID.randomUUID() + "_" + fileName;
    }

    private static String getStoredName(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }

}
