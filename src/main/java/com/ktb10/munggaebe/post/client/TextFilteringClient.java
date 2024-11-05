package com.ktb10.munggaebe.post.client;

import com.ktb10.munggaebe.post.client.dto.FilteringReq;
import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import com.ktb10.munggaebe.post.exception.FastApiClientException;
import com.ktb10.munggaebe.post.exception.FastApiServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextFilteringClient {

    private final WebClient fastApiWebClient;

    public FilteringRes filterText(String text) {
        String uri = "/comments/";
        return fastApiWebClient.post()
                .uri(uri)
                .body(BodyInserters.fromValue(new FilteringReq(text)))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.just(new FastApiClientException()))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.just(new FastApiServerException()))
                .bodyToMono(FilteringRes.class)
                .onErrorResume(e -> {
                    log.warn("FastApi 예외 발생", e);
                    return Mono.just(new FilteringRes(text, List.of("clean"), "예외 발생으로 임시 clean 처리"));
                })
                .block();
    }
}
