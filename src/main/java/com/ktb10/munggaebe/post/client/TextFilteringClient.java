package com.ktb10.munggaebe.post.client;

import com.ktb10.munggaebe.post.client.dto.FilteringReq;
import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
                .bodyToMono(FilteringRes.class)
                .block();
    }
}
