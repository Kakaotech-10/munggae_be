package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.notification.repository.SseEmitterRepository;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
}
