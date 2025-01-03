package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.post.client.TextFilteringClient;
import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilteringService {

    private final TextFilteringClient textFilteringClient;

    public boolean isCleanText(String text) {
        log.info("Filtering text {}", text);

        FilteringRes filteringResult = textFilteringClient.filterText(text);
        log.info("text filtering result = {}", filteringResult);

        List<String> filteredLabels = filteringResult.getFilteredLabels();
        return filteredLabels.size() == 1 && filteredLabels.contains("clean");
    }
}
