package com.ktb10.munggaebe.post.controller;

import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.dto.CommentDto;
import com.ktb10.munggaebe.post.dto.CommentServiceDto;
import com.ktb10.munggaebe.post.service.CommentService;
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
@Tag(name = "Comment API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    private static final String DEFAULT_COMMENT_PAGE_NO = "0";
    private static final String DEFAULT_COMMENT_AGE_SIZE = "10";

    @GetMapping("/comments")
    @Operation(summary = "루트 댓글 조회", description = "주어진 포스트 ID에 대한 루트 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
    public ResponseEntity<Page<CommentDto.CommentRes>> getRootComments(@RequestParam final long postId,
                                                                       @RequestParam(required = false, defaultValue = DEFAULT_COMMENT_PAGE_NO) final int pageNo,
                                                                       @RequestParam(required = false, defaultValue = DEFAULT_COMMENT_AGE_SIZE) final int pageSize) {

        final Page<Comment> comments = commentService.getRootComments(postId, pageNo, pageSize);

        return ResponseEntity.ok(comments.map(CommentDto.CommentRes::new));
    }

    @PostMapping("/comments")
    @Operation(summary = "루트 댓글 생성", description = "새로운 루트 댓글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    public ResponseEntity<CommentDto.CommentRes> createRootComment(@RequestBody final CommentDto.CommentCreateReq request,
                                                                   @RequestParam final long postId,
                                                                   @RequestParam final long memberId) {

        final Comment comment = commentService.createRootComment(request.toEntity(), postId, memberId);

        return ResponseEntity.created(URI.create("/api/v1/comments/" + comment.getId()))
                .body(new CommentDto.CommentRes(comment));
    }

    @GetMapping("/comments/{commentId}")
    @Operation(summary = "댓글 및 대댓글 조회", description = "주어진 댓글 ID에 대한 댓글 및 대댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
    public ResponseEntity<CommentDto.CommentResWithReplies> getRootCommentWithReplies(@PathVariable final long commentId) {

        final Comment comment = commentService.getRootComment(commentId);

        return ResponseEntity.ok(new CommentDto.CommentResWithReplies(comment));
    }

    @PostMapping("/comments/{commentId}")
    @Operation(summary = "대댓글 생성", description = "주어진 댓글에 대댓글을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    public ResponseEntity<CommentDto.CommentRes> createReplyComment(@PathVariable final long commentId,
                                                                    @RequestBody final CommentDto.CommentCreateReq request,
                                                                    @RequestParam final long memberId) {

        final Comment comment = commentService.createReplyComment(request.toEntity(), commentId, memberId);

        return ResponseEntity.created(URI.create("/api/v1/comments/" + comment.getId()))
                .body(new CommentDto.CommentRes(comment));
    }

    @PutMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "주어진 댓글 ID에 대해 댓글 내용을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    public ResponseEntity<CommentDto.CommentRes> updateComment(@PathVariable final long commentId,
                                                               @RequestBody final CommentDto.CommentUpdateReq request,
                                                               @RequestParam final long memberId) {

        final CommentServiceDto.UpdateReq updateReq = toServiceDto(commentId, request);
        final Comment comment = commentService.updateComment(updateReq, memberId);

        return ResponseEntity.ok(new CommentDto.CommentRes(comment));
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "주어진 댓글 ID에 대한 댓글을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "댓글 삭제 성공")
    public ResponseEntity<Void> deleteComment(@PathVariable final long commentId,
                                              @RequestParam final long memberId) {

        commentService.deleteComment(commentId, memberId);

        return ResponseEntity.noContent().build();
    }

    private static CommentServiceDto.UpdateReq toServiceDto(final long commentId, final CommentDto.CommentUpdateReq request) {
        return CommentServiceDto.UpdateReq.builder()
                .commentId(commentId)
                .content(request.getContent())
                .build();
    }

}
