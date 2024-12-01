package com.ktb10.munggaebe.post.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ktb10.munggaebe.post.service.dto.MemberSearchDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    public ElasticsearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<MemberSearchDto> search(String indexName, String keyword) {
        try {
            SearchResponse<MemberSearchDto> response = elasticsearchClient.search(s -> s
                    .index(indexName)
                    .query(q -> q
                            .multiMatch(m -> m
                                    .query(keyword)
                                    .fields(List.of("name", "alias"))
                            )
                    ), MemberSearchDto.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("ElasticSearch search 실패 : indexName = " + indexName + ", keyword = " + keyword, e);
        }
    }
}
