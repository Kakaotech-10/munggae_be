package com.ktb10.munggaebe.notification.domain;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {

    private final ConcurrentHashMap<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        return null;
    }

    public void deleteById(long userId) {

    }

    public Optional<SseEmitter> findById(long userId) {return null;}

    public List<SseEmitter> findAll() {
        return null;
    }
}
