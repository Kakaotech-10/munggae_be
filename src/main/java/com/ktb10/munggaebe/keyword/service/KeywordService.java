package com.ktb10.munggaebe.keyword.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordService {

    private final ElasticsearchKeywordService elasticsearchKeywordService;

    public List<String> getKeywordRankingTopThree() {
        List<Map.Entry<String, Long>> keywordRankings = elasticsearchKeywordService.getTopKeywords(3);
        log.info("Top 3 keyword rankings : {}", keywordRankings);

        List<String> keywords = keywordRankings.stream()
                .map(Map.Entry::getKey)
                .toList();
        log.info("Top 3 keywords : {}", keywords);

        return keywords;
    }
}
