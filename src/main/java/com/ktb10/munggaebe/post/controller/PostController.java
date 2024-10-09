package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Post;
import com.ktb10.munggaebe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(@RequestParam(required = false, defaultValue = "0") final int pageNo,
                                       @RequestParam(required = false, defaultValue = "10") final int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postService.getPosts(pageable);

        return ResponseEntity.ok(posts);
    }
}
