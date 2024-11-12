package com.ktb10.munggaebe.image.service;

import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.MemberImage;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.repository.ImageRepository;
import com.ktb10.munggaebe.image.repository.MemberImageRepository;
import com.ktb10.munggaebe.image.repository.PostImageRepository;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.domain.Member;
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

    @Value("${custom.s3-url}")
    private String s3Url;

    @Value("${custom.cloudfront-url}")
    private String cloudfrontUrl;

    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;
    private final MemberImageRepository memberImageRepository;
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

    public List<String> getPostImageUrls(long postId) {
        log.info("getPostImageUrls start : postId = {}", postId);

        List<String> s3ImagePaths = postImageRepository.findS3ImagePathsByPostId(postId);
        log.info("s3ImagePaths = {}", s3ImagePaths);

        return s3ImagePaths.stream()
                .map(this::convertS3PathToCloudFront)
                .toList();
    }

    public MemberImage saveMemberImage(Member member, UrlDto urls) {
        log.info("saveMemberImage start : urls = {}", urls);

        MemberImage memberImage = MemberImage.builder()
                .member(member)
                .originalName(urls.getFileName())
                .storedName(getStoredName(urls.getUrl()))
                .s3ImagePath(urls.getUrl())
                .build();

        return imageRepository.save(memberImage);
    }

    public String getMemberImageUrl(long memberId) {
        log.info("getMemberImageUrl start : memberId = {}", memberId);

        String s3ImagePath = memberImageRepository.findS3ImagePathsByMemberId(memberId);
        log.info("s3ImagePath = {}", s3ImagePath);

        return convertS3PathToCloudFront(s3ImagePath);
    }

    private String convertS3PathToCloudFront(String s3Path) {
        if (s3Path == null) {
            return null;
        }

        String s3PathWithOutParams = s3Path.split("\\?")[0];
        String cloudFrontPath = s3PathWithOutParams.replace(s3Url, cloudfrontUrl);
        log.info("cloudFrontPath = {}", cloudFrontPath);
        return cloudFrontPath;
    }

    private static String getPrefix(Long id, ImageType imageType) {
        return PREFIX_PATH + imageType.getBasePath() + "/" + id;
    }

    private static String convertToStoredFileName(String fileName) {
        return UUID.randomUUID() + "_" + fileName;
    }

    private static String getStoredName(String url) {
        String[] split = url.split("/");
        return split[split.length - 1].split("\\?")[0];
    }

}
