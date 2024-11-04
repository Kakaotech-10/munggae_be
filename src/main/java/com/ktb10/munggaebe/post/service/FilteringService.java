package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.post.client.TextFilteringClient;
import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilteringService {

    private final TextFilteringClient textFilteringClient;

    public boolean isCleanText(String text) {
        log.info("Filtering text {}", text);

        FilteringRes result = textFilteringClient.filterText(text);
        log.info("text filtering result = {}", result);

        //클린 여부 반환
        return false;
    }
}
