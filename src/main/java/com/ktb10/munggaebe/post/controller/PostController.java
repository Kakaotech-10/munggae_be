package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.dto.PostDto;
import com.ktb10.munggaebe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    private static final String DEFAULT_PAGE_NO = "0";
    private static final String DEFAULT_PAGE_SIZE = "10";

    @GetMapping("/posts")
    public ResponseEntity<Page<PostDto.Res>> getPosts(@RequestParam(required = false, defaultValue = DEFAULT_PAGE_NO) final int pageNo,
                                       @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<Post> posts = postService.getPosts(pageable);

        return ResponseEntity.ok(posts.map(PostDto.Res::new));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto.Res> createPost(@RequestBody final PostDto.CreateReq request, @RequestParam final long memberId) {

        Post createdPost = postService.createPost(request.toEntity(), memberId);

        return ResponseEntity.ok(new PostDto.Res(createdPost));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto.Res> getPost(@PathVariable final long postId) {

        Post post = postService.getPost(postId);

        return ResponseEntity.ok(new PostDto.Res(post));
    }
}
