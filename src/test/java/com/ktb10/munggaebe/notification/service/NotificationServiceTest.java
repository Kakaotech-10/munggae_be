package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.domain.SseEmitterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private SseEmitterRepository emitterRepository;

    @Test
    @DisplayName("구독한 사용자의 emitter를 성공적으로 저장한다.")
    void subscribe_success() {
        // Given
        long lastEventId = 1L;
        SseEmitter emitter = new SseEmitter(1L);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(emitter);
    }

    @Test
    @DisplayName("생성한 emitter의 만료시간은 정해진 시간이다.")
    void subscribe_timeout() {
        // Given
        long lastEventId = 1L;
        long defaultTimeout = 1L;
        SseEmitter emitter = new SseEmitter(defaultTimeout);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTimeout()).isEqualTo(defaultTimeout);
    }

    @Test
    @DisplayName("emitter 완료 시, 저장소에서 삭제한다.")
    void subscribe_delete_whenCompleted() {
        // Given
        long lastEventId = 1L;
        SseEmitter emitter = new SseEmitter(1L);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);
        result.complete();

        // Then
        assertThat(result).isNotNull();
        verify(emitterRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("emitter 타임아웃 시, 저장소에서 삭제한다.")
    void subscribe_delete_whenTimeout() throws InterruptedException {
        // Given
        long lastEventId = 1L;
        SseEmitter emitter = new SseEmitter(1L);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);
        Thread.sleep(5);

        // Then
        assertThat(result).isNotNull();
        verify(emitterRepository).deleteById(lastEventId);
    }

    @Test
    @DisplayName("emitter 에러 발생 시, 저장소에서 삭제한다.")
    void subscribe_delete_whenError() {
        // Given
        long lastEventId = 1L;
        SseEmitter emitter = new SseEmitter(1L);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);

        try {
            result.send(new Object());
        } catch (IOException ignored) {
        }

        // Then
        assertThat(result).isNotNull();
        verify(emitterRepository).deleteById(lastEventId);
    }
}