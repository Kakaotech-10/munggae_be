package com.ktb10.munggaebe.aicomment.service;

import com.ktb10.munggaebe.aicomment.client.AiCommentClient;
import com.ktb10.munggaebe.aicomment.client.dto.CreateAiCommentRes;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.post.domain.Comment;
import com.ktb10.munggaebe.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiCommentService {

    private final AiCommentClient aiCommentClient;
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @Async
    public void createAiComment(String content, long postId) {
        log.info("createAiComment start : content = {}", content);
        CreateAiCommentRes res = aiCommentClient.generateAiComment(content);
        log.info("CreateAiCommentRes res = {}", res);

        Member groomAi = memberRepository.findByNameEnglish("Groom AI")
                .orElseThrow(() -> new MemberNotFoundException(100L));

        Comment aiComment = Comment.builder()
                .member(groomAi)
                .content(res.getResponse())
                .build();
        commentService.createRootComment(aiComment, postId);
    }
}
