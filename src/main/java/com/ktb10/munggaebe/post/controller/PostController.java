package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.dto.PostDto;
import com.ktb10.munggaebe.post.dto.PostServiceDto;
import com.ktb10.munggaebe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    private static final String DEFAULT_POST_PAGE_NO = "0";
    private static final String DEFAULT_POST_PAGE_SIZE = "10";

    @GetMapping("/posts")
    public ResponseEntity<Page<PostDto.Res>> getPosts(@RequestParam(required = false, defaultValue = DEFAULT_POST_PAGE_NO) final int pageNo,
                                       @RequestParam(required = false, defaultValue = DEFAULT_POST_PAGE_SIZE) final int pageSize) {

        final Page<Post> posts = postService.getPosts(pageNo, pageSize);

        return ResponseEntity.ok(posts.map(PostDto.Res::new));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto.Res> createPost(@RequestBody final PostDto.CreateReq request, @RequestParam final long memberId) {

        final Post createdPost = postService.createPost(request.toEntity(), memberId);

        return ResponseEntity.created(URI.create("/api/v1/posts/" + createdPost.getId()))
                .body(new PostDto.Res(createdPost));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto.Res> getPost(@PathVariable final long postId) {

        final Post post = postService.getPost(postId);

        return ResponseEntity.ok(new PostDto.Res(post));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto.Res> updatePost(@PathVariable final long postId,
                                                  @RequestBody final PostDto.UpdateReq request,
                                                  @RequestParam final long memberId) {
        final PostServiceDto.UpdateReq updateReq = toServiceDto(postId, request);
        final Post updatedPost = postService.updatePost(updateReq, memberId);

        return ResponseEntity.ok(new PostDto.Res(updatedPost));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable final long postId, @RequestParam final long memberId) {

        postService.deletePost(postId, memberId);

        return ResponseEntity.noContent().build();
    }

    private static PostServiceDto.UpdateReq toServiceDto(long postId, PostDto.UpdateReq request) {
        return PostServiceDto.UpdateReq.builder()
                .postId(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
