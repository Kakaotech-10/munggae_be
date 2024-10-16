package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.dto.PostDto;
import com.ktb10.munggaebe.post.dto.PostServiceDto;
import com.ktb10.munggaebe.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Post API", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    private static final String DEFAULT_POST_PAGE_NO = "0";
    private static final String DEFAULT_POST_PAGE_SIZE = "10";

    @GetMapping("/posts")
    @Operation(summary = "게시글 목록 조회", description = "페이지 번호와 크기를 기준으로 게시글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
    public ResponseEntity<Page<PostDto.PostRes>> getPosts(@RequestParam(required = false, defaultValue = DEFAULT_POST_PAGE_NO) final int pageNo,
                                                          @RequestParam(required = false, defaultValue = DEFAULT_POST_PAGE_SIZE) final int pageSize) {

        final Page<Post> posts = postService.getPosts(pageNo, pageSize);

        return ResponseEntity.ok(posts.map(PostDto.PostRes::new));
    }

    @PostMapping("/posts")
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    public ResponseEntity<PostDto.PostRes> createPost(@RequestBody final PostDto.PostCreateReq request, @RequestParam final long memberId) {

        final Post createdPost = postService.createPost(request.toEntity(), memberId);

        return ResponseEntity.created(URI.create("/api/v1/posts/" + createdPost.getId()))
                .body(new PostDto.PostRes(createdPost));
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "게시글 조회", description = "주어진 ID를 기준으로 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공")
    public ResponseEntity<PostDto.PostRes> getPost(@PathVariable final long postId) {

        final Post post = postService.getPost(postId);

        return ResponseEntity.ok(new PostDto.PostRes(post));
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "게시글 수정", description = "주어진 ID를 기준으로 게시글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    public ResponseEntity<PostDto.PostRes> updatePost(@PathVariable final long postId,
                                                      @RequestBody final PostDto.PostUpdateReq request,
                                                      @RequestParam final long memberId) {
        final PostServiceDto.UpdateReq updateReq = toServiceDto(postId, request);
        final Post updatedPost = postService.updatePost(updateReq, memberId);

        return ResponseEntity.ok(new PostDto.PostRes(updatedPost));
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "게시글 삭제", description = "주어진 ID를 기준으로 게시글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
    public ResponseEntity<Void> deletePost(@PathVariable final long postId, @RequestParam final long memberId) {

        postService.deletePost(postId, memberId);

        return ResponseEntity.noContent().build();
    }

    private static PostServiceDto.UpdateReq toServiceDto(long postId, PostDto.PostUpdateReq request) {
        return PostServiceDto.UpdateReq.builder()
                .postId(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
