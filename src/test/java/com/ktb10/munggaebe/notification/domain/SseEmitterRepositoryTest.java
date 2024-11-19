package com.ktb10.munggaebe.notification.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SseEmitterRepositoryTest {

    @Autowired
    private SseEmitterRepository repository;

    @BeforeEach
    void setUp() {
        repository.clear();
    }

    @Test
    @DisplayName("emitter를 성공적으로 저장한다.")
    void save_success() {
        // Given
        long userId = 1L;
        SseEmitter sseEmitter = new SseEmitter();

        // When
        SseEmitter savedEmitter = repository.save(userId, sseEmitter);

        // Then
        SseEmitter result = repository.findById(userId).orElse(null);
        assertThat(result).isEqualTo(sseEmitter);
    }

    @Test
    @DisplayName("저장한 emitter를 반환한다.")
    void save_return() {
        // Given
        long userId = 1L;
        SseEmitter sseEmitter = new SseEmitter();

        // When
        SseEmitter savedEmitter = repository.save(userId, sseEmitter);

        // Then
        SseEmitter result = repository.findById(userId).orElse(null);
        assertThat(result).isEqualTo(savedEmitter);
    }

    @Test
    @DisplayName("userId에 해당하는 emitter를 반환한다.")
    void findById_success() {
        // Given
        long userId = 1L;
        SseEmitter sseEmitter = new SseEmitter();
        SseEmitter savedEmitter = repository.save(userId, sseEmitter);

        // When
        Optional<SseEmitter> result = repository.findById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedEmitter);
    }

    @Test
    @DisplayName("userId에 해당하는 emitter가 없으면 빈 Optional을 반환한다.")
    void findById_empty() {
        // Given
        long userId = 1L;

        // When
        Optional<SseEmitter> result = repository.findById(userId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("userId에 해당하는 emitter를 성공적으로 삭제한다.")
    void deleteById_success() {
        // Given
        long userId = 1L;
        SseEmitter sseEmitter = new SseEmitter();
        SseEmitter savedEmitter = repository.save(userId, sseEmitter);

        // When
        repository.deleteById(userId);

        // Then
        Optional<SseEmitter> result = repository.findById(userId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("저장된 모든 emitter를 반환한다.")
    void findAll_success() {
        // Given
        SseEmitter sseEmitter1 = new SseEmitter();
        SseEmitter sseEmitter2 = new SseEmitter();
        repository.save(1L, sseEmitter1);
        repository.save(2L, sseEmitter2);

        // When
        List<SseEmitter> result = repository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(sseEmitter1, sseEmitter2);
    }
}