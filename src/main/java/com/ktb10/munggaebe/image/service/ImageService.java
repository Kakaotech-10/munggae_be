package com.ktb10.munggaebe.image.service;

import com.ktb10.munggaebe.image.domain.Image;
import com.ktb10.munggaebe.image.domain.ImageType;
import com.ktb10.munggaebe.image.domain.MemberImage;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.repository.ImageRepository;
import com.ktb10.munggaebe.image.repository.MemberImageRepository;
import com.ktb10.munggaebe.image.repository.PostImageRepository;
import com.ktb10.munggaebe.image.service.dto.ImageCdnPathDto;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public List<PostImage> savePostImages(Post post, List<UrlDto> urlDtos) {
        log.info("savePostImages start : urls = {}", urlDtos);
        List<PostImage> images = urlDtos.stream()
                .map(u -> PostImage.builder()
                        .post(post)
                        .originalName(u.getFileName())
                        .storedName(getStoredName(u.getUrl()))
                        .s3ImagePath(u.getUrl())
                        .build())
                .toList();

        return imageRepository.saveAll(images);
    }

    public List<ImageCdnPathDto> getPostImageUrls(long postId) {
        log.info("getPostImageUrls start : postId = {}", postId);

        List<PostImage> postImages = postImageRepository.findAllByPostId(postId);
        log.info("postImages.size = {}", postImages.size());

        return postImages.stream()
                .map(pi -> ImageCdnPathDto.builder()
                        .imageId(pi.getId())
                        .fileName(pi.getOriginalName())
                        .path(convertS3PathToCloudFront(pi.getS3ImagePath()))
                        .build()
                )
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

    public ImageCdnPathDto getMemberImageUrl(long memberId) {
        log.info("getMemberImageUrl start : memberId = {}", memberId);

        Optional<MemberImage> optionalMemberImage = memberImageRepository.findByMemberId(memberId);

        if (optionalMemberImage.isEmpty()) {
            log.info("memberImage isEmpty");
            return null;
        }

        MemberImage memberImage = optionalMemberImage.get();

        return ImageCdnPathDto.builder()
                .imageId(memberImage.getId())
                .fileName(memberImage.getOriginalName())
                .path(convertS3PathToCloudFront(memberImage.getS3ImagePath()))
                .build();
    }

    public Image updateImage(long imageId, UrlDto imageInfo) {
        log.info("updateImage start : imageId = {}, imageInfo.fileName = {}, imageInfo.url = {}", imageId, imageInfo.getFileName(), imageInfo.getUrl());

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NoSuchElementException("Image not found"));
        validateUrl(imageInfo.getUrl(), image);

        final String prevS3ImagePath = image.getS3ImagePath();

        image.update(imageInfo.getFileName(), getStoredName(imageInfo.getUrl()), imageInfo.getUrl());

        s3Service.deleteImage(prevS3ImagePath);

        return image;
    }

    private static void validateUrl(String url, Image image) {
        if (getStoredName(url).equals(image.getStoredName())) {
            throw new IllegalArgumentException("잘못된 Url 입력입니다.");
        }
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
