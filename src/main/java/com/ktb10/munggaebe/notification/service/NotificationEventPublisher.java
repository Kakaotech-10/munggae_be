package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.domain.NotificationType;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String NOTIFICATION_TOPIC = "notifications";

    public NotificationEvent createBroadCastingEvent(NotificationType notificationType, String message) {
        return NotificationEvent.builder()
                .type(notificationType)
                .message(message)
                .build();
    }

    public NotificationEvent createUniCastingEvent(Long receiverId, NotificationType notificationType, String message) {
        return NotificationEvent.builder()
                .receiverId(receiverId)
                .type(notificationType)
                .message(message)
                .build();
    }

    public void publishEvent(NotificationEvent event) {
        redisTemplate.convertAndSend(NOTIFICATION_TOPIC, event);
    }
}
