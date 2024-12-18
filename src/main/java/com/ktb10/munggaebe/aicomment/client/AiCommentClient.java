package com.ktb10.munggaebe.aicomment.client;

import com.ktb10.munggaebe.aicomment.client.dto.CreateAiCommentReq;
import com.ktb10.munggaebe.aicomment.client.dto.CreateAiCommentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiCommentClient {

    private final WebClient aiCommentWebClient;

    public CreateAiCommentRes generateAiComment(String content) {
        String uri = "/api/codereview/v1";
        return aiCommentWebClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(new CreateAiCommentReq(content)))
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.just(new RuntimeException()))
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.just(new RuntimeException()))
                .bodyToMono(CreateAiCommentRes.class)
                .block();
    }
}
