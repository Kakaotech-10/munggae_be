package com.ktb10.munggaebe.keyword.controller;

import com.ktb10.munggaebe.keyword.controller.dto.KeywordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/keywords")
@Tag(name = "Keyword API", description = "키워드 관련 API")
public class KeywordController {

    @GetMapping("/ranking")
    @Operation(summary = "키워드 랭킹 탑3", description = "키워드 랭킹 탑3 조회해 반환합니다.")
    @ApiResponse(responseCode = "200", description = "키워드 랭킹 탑3 조회 성공")
    public ResponseEntity<KeywordDto.KeywordRankingTopThreeRes> keywordRankingTopThree() {
        return ResponseEntity.ok().build();
    }
}
