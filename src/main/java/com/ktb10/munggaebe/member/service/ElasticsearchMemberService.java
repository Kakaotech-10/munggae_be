package com.ktb10.munggaebe.member.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchMemberService {

    private final ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "members";

    public void addMember(String name, String nameEnglish) {
        try {
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index(INDEX_NAME)
                    .document(Map.of(
                            "name", name,
                            "alias", nameEnglish
                    ))
            );

            log.info("Document added to index '{}': ID = {}, Result = {}", INDEX_NAME, response.id(), response.result());
        } catch (IOException e) {
            log.error("Failed to add document to index '{}': {}", INDEX_NAME, e.getMessage(), e);
            throw new RuntimeException("Failed to add document to Elasticsearch", e);
        }
    }
}
