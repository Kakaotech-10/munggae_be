package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.post.service.dto.MemberSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentionService {

    private final ElasticsearchService elasticsearchService;

    private static final String INDEX_NAME = "members";

    public List<MemberSearchDto> searchMemberName(String keyword) {
        log.info("searchMemberName start : keyword = {}", keyword);
        List<MemberSearchDto> result = elasticsearchService.search(INDEX_NAME, keyword);
        log.info("searchMemberName result = {}", result);
        return result;
    }
}
