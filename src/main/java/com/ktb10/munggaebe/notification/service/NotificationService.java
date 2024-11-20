package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.repository.SseEmitterRepository;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterRepository emitterRepository;

    private static final long DEFAULT_TIMEOUT = 10 * 60 * 1000; //10m

    public SseEmitter subscribe(long lastEventId) {

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        Long userId = SecurityUtil.getCurrentUserId();

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));
        emitter.onError((e) -> emitterRepository.deleteById(userId));

        //NotificationRepository에서 lastEventId 이후의 notifications를 해당 유저에게 다시 전송

        return emitterRepository.save(userId, emitter);
    }

    public void handleNotificationEvent(NotificationEvent event) {
        log.info("handleNotificationEvent start: event = {}", event);
        CompletableFuture<Void> dbTask = saveNotification(event);
        CompletableFuture<Void> sendTask = sendNotification(event);

        CompletableFuture.allOf(dbTask, sendTask)
                .thenRun(() -> log.info("모든 병렬 처리 완료 : event = {}", event))
                .exceptionally(e -> {
                    log.error("Error Notification 병렬 처리: {}", e.getMessage(), e);
                    return null;
                });
    }

    @Async("notificationAsyncExecutor")
    public CompletableFuture<Void> sendNotification(NotificationEvent event) {
        return null;
    }

    @Async("notificationAsyncExecutor")
    public CompletableFuture<Void> saveNotification(NotificationEvent event) {
        return null;
    }
}
