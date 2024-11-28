package com.ktb10.munggaebe.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SseEmitterRepository {

    private final ConcurrentHashMap<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter save(Long userId, SseEmitter sseEmitter) {
        SseEmitter removedEmitter = userEmitters.remove(userId);
        if (removedEmitter != null) {
            removedEmitter.complete();
        }

        userEmitters.put(userId, sseEmitter);

        return findById(userId).orElseThrow(() -> new IllegalStateException("저장 실패"));
    }

    public void deleteById(long userId) {
        userEmitters.remove(userId);
    }

    public Optional<SseEmitter> findById(long userId) {return Optional.ofNullable(userEmitters.get(userId));}

    public List<SseEmitter> findAll() {
        return new ArrayList<>(userEmitters.values());
    }

    public List<Map.Entry<Long, SseEmitter>> findAllEntries() {
        return new ArrayList<>(userEmitters.entrySet());
    }

    public void clear() {
        userEmitters.clear();
    }
}
