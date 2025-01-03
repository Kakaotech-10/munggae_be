package com.ktb10.munggaebe.config;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${custom.fast-api.base-url}")
    private String baseUrl;

    @Value("${custom.ai-comment-api.base-url}")
    private String aiCommentAPIBaseUrl;

    @Bean
    public WebClient textFilteringWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }

    @Bean
    public WebClient aiCommentWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(aiCommentAPIBaseUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }
}
