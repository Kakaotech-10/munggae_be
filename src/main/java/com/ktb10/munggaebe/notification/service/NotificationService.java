package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.domain.Notification;
import com.ktb10.munggaebe.notification.repository.SseEmitterRepository;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterRepository emitterRepository;
    private final NotificationPersistenceService notificationPersistenceService;

    private static final long DEFAULT_TIMEOUT = 10 * 60 * 1000; //10m

    public SseEmitter subscribe(String lastEventId) {

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        Long userId = SecurityUtil.getCurrentUserId();

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));
        emitter.onError((e) -> emitterRepository.deleteById(userId));

        SseEmitter savedEmitter = emitterRepository.save(userId, emitter);

        sendToEmitter(userId, savedEmitter, "SSE 연결 성공 : userId = " + userId, "connect_success");

        if (!lastEventId.isBlank()) {
            List<Notification> missingNotifications = notificationPersistenceService.getMissingNotifications(userId, lastEventId);
            missingNotifications.forEach(notify -> sendToEmitter(userId, savedEmitter, notify.getMessage(), "notification"));
        }

        return savedEmitter;
    }

    public void handleNotificationEvent(NotificationEvent event) {
        log.info("handleNotificationEvent start: event = {}", event);
        validateNotificationEvent(event);

        sendNotification(event);
    }

    public void sendNotification(NotificationEvent event) {
        if (event.getReceiverId() == null) {
            sendBroadCasting(event);
            return;
        }
        sendUniCasting(event);
    }

    public void saveNotification(NotificationEvent event) {
        if (event.getReceiverId() == null) {
            notificationPersistenceService.saveBroadCasting(event);
            return;
        }
        notificationPersistenceService.saveUnicasting(event);
    }

    @Async("notificationAsyncExecutor")
    public CompletableFuture<Void> sendNotificationAsync(NotificationEvent event) {
        return CompletableFuture.runAsync(() -> {
            if (event.getReceiverId() == null) {
                sendBroadCasting(event);
                return;
            }
            sendUniCasting(event);
        });
    }

    @Async("notificationAsyncExecutor")
    public CompletableFuture<Void> saveNotificationAsync(NotificationEvent event) {
        return CompletableFuture.runAsync(() -> {
            if (event.getReceiverId() == null) {
                notificationPersistenceService.saveBroadCasting(event);
                return;
            }
            notificationPersistenceService.saveUnicasting(event);
        });
    }

    private static void validateNotificationEvent(NotificationEvent event) {
        if (event.getType() == null || event.getMessage() == null) {
            throw new IllegalArgumentException("잘못된 입력입니다. type = " + event.getType() + ", message = " + event.getMessage());
        }
    }

    private void sendBroadCasting(NotificationEvent event) {
        log.info("sendBroadCasting start");
        List<Map.Entry<Long, SseEmitter>> emitterEntries = emitterRepository.findAllEntries();
        emitterEntries.forEach(entry -> sendToEmitter(entry.getKey(), entry.getValue(), event.getMessage(), "notification"));
    }

    private void sendUniCasting(NotificationEvent event) {
        log.info("sendUniCasting start: receiverId = {}", event.getReceiverId());
        Optional<SseEmitter> emiiter = emitterRepository.findById(event.getReceiverId());
        emiiter.ifPresent(emitter -> sendToEmitter(event.getReceiverId(), emitter, event.getMessage(), "notification"));
    }

    private void sendToEmitter(long userId, SseEmitter emitter, String message, String eventName) {
        try {
            emitter.send(SseEmitter.event().name(eventName).data(message));
            log.info("sendToEmitter : userId = {}, eventName = {}, message = {}", userId, eventName, message);
        } catch (IOException e) {
            emitterRepository.deleteById(userId);
            throw new RuntimeException("알람 전송 중 문제 발생", e);
        }
    }
}
