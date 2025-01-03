package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.post.client.TextFilteringClient;
import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FilteringServiceTest {

    @InjectMocks
    private FilteringService filteringService;

    @Mock
    private TextFilteringClient textFilteringClient;

    @Test
    @DisplayName("클린한 텍스트가 들어오면 true를 반환한다.")
    void isCleanText_success_clean() {
        // Given
        String text = "clean text";
        FilteringRes res = new FilteringRes(text, List.of("clean"), "message");

        given(textFilteringClient.filterText(text)).willReturn(res);

        // When
        boolean result = filteringService.isCleanText(text);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("클린하지 않은 텍스트가 들어오면 true를 반환한다.")
    void isCleanText_success_notClean() {
        // Given
        String text = "bad word";
        FilteringRes res = new FilteringRes(text, List.of("악플/욕설"), "message");

        given(textFilteringClient.filterText(text)).willReturn(res);

        // When
        boolean result = filteringService.isCleanText(text);

        // Then
        assertThat(result).isFalse();
    }
}