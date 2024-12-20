package com.ktb10.munggaebe.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.image.domain.PostImage;
import com.ktb10.munggaebe.image.service.dto.UrlDto;
import com.ktb10.munggaebe.post.controller.dto.PostDto;
import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.service.PostService;
import com.ktb10.munggaebe.post.service.dto.PostServiceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Post API", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_POST_PAGE_NO = "0";
    private static final String DEFAULT_POST_PAGE_SIZE = "10";


    //채널마다 게시글 조회 (GET /api/v1/posts)
    @GetMapping("/posts")
    @Operation(summary = "채널마다 게시글 목록 조회", description = "채널 ID를 기준으로 게시글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    public ResponseEntity<Page<PostDto.PostRes>> getPosts(
            @RequestParam(defaultValue = "1") Long channelId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Post> posts = postService.getPosts(channelId, pageNo, pageSize);
        Page<PostDto.PostRes> postDtos = posts.map(this::appendCdnPaths);

        return ResponseEntity.ok(postDtos);
    }

    //특정 채널에 게시글 생성 (POST /api/v1/posts)
    @PostMapping("/posts")
    @Operation(summary = "채널마다 게시글 생성", description = "특정 채널에 게시글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    public ResponseEntity<PostDto.PostRes> createPost(
            @RequestParam Long channelId,
            @RequestBody PostDto.PostCreateReq request) {

        Post post = request.toEntity();
        Post createdPost = postService.createPost(channelId, post);

        return ResponseEntity.created(URI.create("/api/v1/posts/" + createdPost.getId()))
                .body(new PostDto.PostRes(createdPost));
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "게시글 조회", description = "주어진 Post ID를 기준으로 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
    public ResponseEntity<PostDto.PostRes> getPost(@PathVariable final long postId) {

        final Post post = postService.getPost(postId);

        return ResponseEntity.ok(appendCdnPaths(post));
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "게시글 수정", description = "주어진 ID를 기준으로 게시글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    public ResponseEntity<PostDto.PostRes> updatePost(@PathVariable final long postId,
                                                      @RequestBody final PostDto.PostUpdateReq request) {
        final PostServiceDto.UpdateReq updateReq = toServiceDto(postId, request);
        final Post updatedPost = postService.updatePost(updateReq);

        return ResponseEntity.ok(appendCdnPaths(updatedPost));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 삭제", description = "주어진 ID를 기준으로 게시글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    public ResponseEntity<Void> deletePost(@PathVariable final long postId) {

        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{postId}/images/presigned-url")
    @Operation(summary = "게시글 이미지 사전 서명 url 생성", description = "S3로부터 사전 서명 url을 생성해 반환합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 이미지 사전 서명 url 생성 성공")
    public ResponseEntity<PostDto.ImagePresignedUrlRes> getPresignedUrl(@PathVariable final long postId,
                                             @RequestBody final PostDto.ImagePresignedUrlReq request) {
        final List<PostServiceDto.PresignedUrlRes> urlRes = postService.getPresignedUrl(postId, request.getFileNames());

        final List<UrlDto> urls = urlRes.stream()
                .map(o -> objectMapper.convertValue(o, UrlDto.class))
                .toList();

        return ResponseEntity.ok(
                PostDto.ImagePresignedUrlRes.builder()
                        .count(urls.size())
                        .urls(urls)
                        .build()
        );
    }

    @PostMapping("/posts/{postId}/images")
    @Operation(summary = "게시글 이미지 저장", description = "게시물 이미지 이름과 s3 url을 저장합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 이미지 저장 성공")
    public ResponseEntity<PostDto.ImageSaveRes> savePostImages(@PathVariable final long postId,
                                            @RequestBody final PostDto.ImageSaveReq request) {

        final List<PostImage> postImages = postService.saveImages(postId, request.getUrls());

        return ResponseEntity.created(URI.create("/api/v1/posts/" + postId))
                .body(new PostDto.ImageSaveRes(postImages));
    }

    @PutMapping("/posts/{postId}/images/{imageId}")
    @Operation(summary = "게시글 이미지 수정", description = "주어진 이미지 id를 통해 이미지를 수정합니다")
    @ApiResponse(responseCode = "200", description = "게시글 이미지 수정 성공")
    public ResponseEntity<PostDto.ImageRes> updatePostImage(@PathVariable final long postId,
                                             @PathVariable final long imageId,
                                             @RequestBody final PostDto.ImageUpdateReq request) {

        final PostImage postImage = postService.updateImage(postId, imageId, request.getImageInfo());

        return ResponseEntity.ok(new PostDto.ImageRes(postImage));
    }

    @GetMapping("/posts/announcements/near-deadline")
    @Operation(summary = "마감되지 않은 공지사항 조회", description = "마감되지 않은 공지사항 게시물을 마감임박순으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "마감되지 않은 공지사항 조회 성공")
    public ResponseEntity<Page<PostDto.PostRes>> getAnnouncementsPostsNearDeadline(@RequestParam(required = false, defaultValue = DEFAULT_POST_PAGE_NO) final int pageNo,
                                                                                   @RequestParam(required = false, defaultValue = DEFAULT_POST_PAGE_SIZE) final int pageSize) {

        Page<Post> posts = postService.getAnnouncementsPostsNearDeadline(pageNo, pageSize);

        return ResponseEntity.ok(posts.map(this::appendCdnPaths));
    }

    private static PostServiceDto.UpdateReq toServiceDto(final long postId, final PostDto.PostUpdateReq request) {
        return PostServiceDto.UpdateReq.builder()
                .postId(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

    private PostDto.PostRes appendCdnPaths(final Post p) {
        final List<PostServiceDto.ImageCdnPathRes> postImageCdnPaths = postService.getPostImageCdnPaths(p.getId());
        final List<PostDto.ImageCdnPathRes> imageCdnPathRes = postImageCdnPaths.stream()
                .map(pi -> objectMapper.convertValue(pi, PostDto.ImageCdnPathRes.class))
                .toList();
        return new PostDto.PostRes(p, imageCdnPathRes);
    }
}
