package com.ktb10.munggaebe.image.service;

import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        String url = s3Service.generatePresignedUrl(prefix, fileName);
        log.info("url = {}", url);

        return url;
    }

    private static String getPrefix(Long id, ImageType imageType) {
        return PREFIX_PATH + imageType.getBasePath() + "/" + id;
    }


    //S3에 저장된 이미지 URL, fileName 받아서 저장, 한번에 받게 될 수도 있음.
}
