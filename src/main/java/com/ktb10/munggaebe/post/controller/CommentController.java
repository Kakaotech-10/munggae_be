package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.dto.CommentDto;
import com.ktb10.munggaebe.post.dto.CommentServiceDto;
import com.ktb10.munggaebe.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @PostMapping("/comments")
    public ResponseEntity<CommentDto.Res> createRootComment(@RequestBody final CommentDto.CreateReq request,
                                                            @RequestParam final long postId,
                                                            @RequestParam final long memberId) {

        Comment comment = commentService.createRootComment(request.toEntity(), postId, memberId);

        return ResponseEntity.created(URI.create("/api/v1/comments/" + comment.getId()))
                .body(new CommentDto.Res(comment));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto.ResWithReplies> getRootCommentWithReplies(@PathVariable final long commentId) {

        Comment comment = commentService.getRootComment(commentId);

        return ResponseEntity.ok(new CommentDto.ResWithReplies(comment));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto.Res> createReplyComment(@PathVariable final long commentId,
                                                             @RequestBody final CommentDto.CreateReq request,
                                                             @RequestParam final long memberId) {

        Comment comment = commentService.createReplyComment(request.toEntity(), commentId, memberId);

        return ResponseEntity.created(URI.create("/api/v1/comments/" + comment.getId()))
                .body(new CommentDto.Res(comment));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto.Res> updateComment(@PathVariable final long commentId,
                                                        @RequestBody final CommentDto.UpdateReq request,
                                                        @RequestParam final long memberId) {

        CommentServiceDto.UpdateReq updateReq = toServiceDto(commentId, request);
        Comment comment = commentService.updateComment(updateReq, memberId);

        return ResponseEntity.ok(new CommentDto.Res(comment));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable final long commentId,
                                              @RequestParam final long memberId) {

        commentService.deleteComment(commentId, memberId);

        return ResponseEntity.noContent().build();
    }

    private static CommentServiceDto.UpdateReq toServiceDto(final long commentId, final CommentDto.UpdateReq request) {
        return CommentServiceDto.UpdateReq.builder()
                .commentId(commentId)
                .content(request.getContent())
                .build();
    }

}
