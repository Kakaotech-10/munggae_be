package com.ktb10.munggaebe.keyword.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KeywordDto {

    @Schema(description = "키워드 랭킹 탑3 응답")
    @Getter
    public static class KeywordRankingTopThreeRes {
        @Schema(description = "키워드 랭킹 리스트", example = "[{\"rank\": 1, \"keyword\": \"키워드1\"}, {\"rank\": 2, \"keyword\": \"키워드2\"}, {\"rank\": 3, \"keyword\": \"키워드3\"}]")
        private List<KeywordRanking> keywordRankings;

        public KeywordRankingTopThreeRes(List<String> keywords) {
            this.keywordRankings = new ArrayList<>();
            AtomicInteger index = new AtomicInteger(1);
            keywords.stream()
                    .map(k -> new KeywordRanking(index.getAndIncrement(), k))
                    .forEach(this.keywordRankings::add);
        }
    }

    @Schema(description = "키워드 랭킹")
    @Getter
    public static class KeywordRanking {
        @Schema(description = "랭킹")
        private int rank;

        @Schema(description = "키워드")
        private String keyword;

        @Builder
        public KeywordRanking(int rank, String keyword) {
            this.rank = rank;
            this.keyword = keyword;
        }
    }
}
