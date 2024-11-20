package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    public void onMessage(NotificationEvent event) {
        log.info("Event received: {}", event);
        notificationService.handleNotificationEvent(event);
    }
}
