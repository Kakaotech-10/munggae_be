package com.ktb10.munggaebe.post.client;

import com.ktb10.munggaebe.post.client.dto.FilteringRes;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TextFilteringClientTest {

    @Autowired
    private TextFilteringClient textFilteringClient;

    private MockWebServer mockServer;

    @BeforeEach
    void setUp() throws IOException {

        mockServer = new MockWebServer();
        mockServer.start(8081);
    }

    @AfterEach
    void terminate() throws IOException {
        mockServer.shutdown();
    }

    @Test
    @DisplayName("텍스트 필터링 결과를 반환한다.")
    void filterText_success() {
        // Given
        String response =
                """
                    {
                        "originText": "clean text",
                        "filteredLabels": ["clean"],
                        "message": "this is message."
                    }
                """;

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(response)
                .addHeader("Content-Type", "application/json"));

        FilteringRes expected = new FilteringRes("clean text", List.of("clean"), "this is message.");

        // When
        FilteringRes result = textFilteringClient.filterText("clean text");

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 403, 404, 500})
    @DisplayName("4xx, 5xx 상태코드로 반환 시, 임의로 clean 처리한 결과를 반환한다.")
    void filterText_4XX_5XX_Error(int statusCode) {
        // Given
        mockServer.enqueue(new MockResponse()
                .setResponseCode(statusCode));

        FilteringRes expected = new FilteringRes("test text", List.of("clean"), "예외 발생으로 임시 clean 처리");

        // When
        FilteringRes result = textFilteringClient.filterText("test text");

        // Then
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }
}