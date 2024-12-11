package com.ktb10.munggaebe.keyword.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchKeywordService {

    private final ElasticsearchClient elasticsearchClient;

    private final static String INDEX_NAME = "keywords";

    public List<Map.Entry<String, Long>> getTopKeywords(int topN) {
        try {
            // Terms Aggregation 설정
            TermsAggregation termsAggregation = TermsAggregation.of(t -> t
                    .field("keyword")
                    .size(topN)
            );

            // Search 요청 생성
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(INDEX_NAME)
                    .size(0) // Aggregation만 수행
                    .aggregations("top_keywords", a -> a.terms(termsAggregation))
            );

            // Elasticsearch 클라이언트를 사용해 요청 실행
            SearchResponse<Void> response = elasticsearchClient.search(searchRequest, Void.class);

            // Aggregation 결과 추출
            List<StringTermsBucket> buckets = response.aggregations()
                    .get("top_keywords")
                    .sterms()
                    .buckets().array();

            // 키워드와 개수 추출
            return buckets.stream()
                    .map(bucket -> Map.entry(bucket.key().stringValue(), bucket.docCount()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("키워드 조회 중 오류 발생: ", e);
            throw new RuntimeException("키워드 조회에 실패했습니다.", e);
        }
    }
}
