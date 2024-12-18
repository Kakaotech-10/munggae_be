package com.ktb10.munggaebe.aicomment.service;

import com.ktb10.munggaebe.aicomment.client.AiCommentClient;
import com.ktb10.munggaebe.aicomment.client.dto.CreateAiCommentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiCommentService {

    private final AiCommentClient aiCommentClient;

//    @Async
    public void createAiComment(String content) {
        log.info("createAiComment start : content = {}", content);
        CreateAiCommentRes res = aiCommentClient.generateAiComment(content);
    }
}
