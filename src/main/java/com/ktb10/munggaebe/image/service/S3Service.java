package com.ktb10.munggaebe.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private static final int PRE_SIGNED_URL_EXPIRE_TIME = 1000 * 60 * 2; //2min

    public String generatePresignedUrl(String prefix, String fileName) {
        log.info("generatePresignedUrl start : prefix = {}, fileName = {}, bucket = {}", prefix, fileName, bucket);

        String filePath = getFilePath(prefix, fileName);
        log.info("filePath = {}", filePath);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(bucket, filePath);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    public void deleteImage(String url) {
        log.info("deleteImage start : url = {}", url);
        try {
            String path = new URI(url).getPath().substring(1);
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, path));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 Url 형식입니다.");
        }
    }

    private static String getFilePath(String prefix, String fileName) {
        return prefix + "/" + fileName;
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String filePath) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, filePath)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());

        //나중에 이미지 확장자에 따라, content_type도 추가하면 좋을듯
        //PublicRead ACL 설정
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += PRE_SIGNED_URL_EXPIRE_TIME;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
}
