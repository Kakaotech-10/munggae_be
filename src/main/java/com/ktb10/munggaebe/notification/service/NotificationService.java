package com.ktb10.munggaebe.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationService {

    //구독한 맴버 레포지토리로 저장
    public SseEmitter subscribe(long lastEventId) {
        return null;
    }
}
