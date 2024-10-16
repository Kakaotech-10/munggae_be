package com.ktb10.munggaebe.error;

import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.post.exception.CommentNotFoundException;
import com.ktb10.munggaebe.post.exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorExceptionController {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        final ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        log.warn(e.getMessage(), e);

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(MemberPermissionDeniedException.class)
    public ResponseEntity<ErrorResponse> handleMemberPermissionDeniedException(MemberPermissionDeniedException e) {
        final ErrorCode errorCode = ErrorCode.MEMBER_PERMISSION_DENIED_EXCEPTION;
        log.warn(e.getMessage(), e);

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException e) {
        final ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;
        log.warn(e.getMessage(), e);

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        final ErrorCode errorCode = ErrorCode.COMMENT_NOT_FOUND;
        log.warn(e.getMessage(), e);

        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.from(errorCode));
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage(), e);
        return ErrorResponse.from(errorCode);
    }
}
