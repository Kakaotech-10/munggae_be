package com.ktb10.munggaebe.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.notification.domain.NotificationType;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @Qualifier("redisPubSubTemplate")
    private final RedisTemplate<String, Object> redisPubSubTemplate;

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

    @Transactional
    public void publishEvent(NotificationEvent event) {
        log.info("publishEvent start : event = {}", event);
        try {
            Long lastEventId = notificationService.saveNotification(event);
            event.setLastEventId(lastEventId);
            String jsonEvent = objectMapper.writeValueAsString(event);
            Long count = redisPubSubTemplate.convertAndSend(NOTIFICATION_TOPIC, jsonEvent);
            log.info("clients count = {}", count);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 문제 발생");
        }

    }
}
