package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.dto.CommentDto;
import com.ktb10.munggaebe.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    private static final String DEFAULT_COMMENT_PAGE_NO = "0";
    private static final String DEFAULT_COMMENT_AGE_SIZE = "10";

    @GetMapping("/comments")
    public ResponseEntity<Page<CommentDto.Res>> getRootComments(@RequestParam final long postId,
                                         @RequestParam(required = false, defaultValue = DEFAULT_COMMENT_PAGE_NO) final int pageNo,
                                         @RequestParam(required = false, defaultValue = DEFAULT_COMMENT_AGE_SIZE) final int pageSize) {

        Page<Comment> comments = commentService.getRootComments(postId, pageNo, pageSize);

        return ResponseEntity.ok(comments.map(CommentDto.Res::new));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto.ResWithReplies> getRootCommentWithReplies(@PathVariable final long commentId) {

        Comment comment = commentService.getRootComment(commentId);

        return ResponseEntity.ok(new CommentDto.ResWithReplies(comment));
    }
}
