package com.ktb10.munggaebe.post.client;

import com.ktb10.munggaebe.post.client.dto.FilteringReq;
import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TextFilteringClient {

    private final WebClient fastApiWebClient;

    public Mono<FilteringRes> filterText(String text) {
        String uri = "comments";
        return fastApiWebClient.post()
                .uri(uri)
                .body(new FilteringReq(text), FilteringReq.class)
                .retrieve()
                .bodyToMono(FilteringRes.class);
    }
}
