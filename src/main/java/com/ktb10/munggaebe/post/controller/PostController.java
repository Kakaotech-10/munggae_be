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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
