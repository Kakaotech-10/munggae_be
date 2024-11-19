package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationEventConsumer {

    public void onMessage(NotificationEvent message) {
        log.info("Message received: {}", message);
        //메시지 큐로부터 받아서, 알람 전송
    }
}
