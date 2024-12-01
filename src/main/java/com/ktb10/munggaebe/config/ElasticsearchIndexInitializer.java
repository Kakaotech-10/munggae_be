package com.ktb10.munggaebe.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ElasticsearchIndexInitializer {

    private final ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "members";

    public ElasticsearchIndexInitializer(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    public void createIndexOnStartup() {
        try {
            // 인덱스 생성
            boolean exists = elasticsearchClient.indices().exists(e -> e.index(INDEX_NAME)).value();
            if (!exists) {
                CreateIndexResponse response = elasticsearchClient.indices().create(c -> c
                        .index(INDEX_NAME)
                        .settings(s -> s
                                .analysis(a -> a
                                        .analyzer("name_analyzer", an -> an
                                                .custom(cu -> cu
                                                        .tokenizer("nori_tokenizer")
                                                        .filter(List.of("lowercase", "edge_ngram_filter"))
                                                )
                                        )
                                        .analyzer("alias_analyzer", an -> an
                                                .custom(cu -> cu
                                                        .tokenizer("standard")
                                                        .filter(List.of("lowercase", "edge_ngram_filter"))
                                                )
                                        )
                                        .filter("edge_ngram_filter", f -> f
                                                .definition(d -> d
                                                        .edgeNgram(e -> e
                                                                .minGram(1)
                                                                .maxGram(20))
                                                )
                                        )
                                )
                        )
                );
                System.out.println("Index created: " + response.acknowledged());
            }

            // 매핑 설정
            PutMappingResponse mappingResponse = elasticsearchClient.indices().putMapping(p -> p
                    .index(INDEX_NAME)
                    .properties("name", pr -> pr
                            .text(t -> t.analyzer("name_analyzer"))
                    )
                    .properties("alias", pr -> pr
                            .text(t -> t.analyzer("alias_analyzer"))
                    )
            );

            log.info("ElasticSearch 인덱스 생성 & 매핑 설정 완료 : " + mappingResponse.acknowledged());
        } catch (IOException e) {
            throw new RuntimeException("ElasticSearch 인덱스 생성 실패: " + INDEX_NAME, e);
        }
    }
}
