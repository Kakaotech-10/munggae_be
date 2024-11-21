package com.ktb10.munggaebe.notification.controller;

import com.ktb10.munggaebe.notification.domain.NotificationType;
import com.ktb10.munggaebe.notification.service.NotificationEventPublisher;
import com.ktb10.munggaebe.notification.service.NotificationService;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationEventPublisher publisher;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 SSE 연결", description = "알림 SSE 연결 시도합니다.")
    @ApiResponse(responseCode = "200", description = "알림 SSE 연결 성공")
    public ResponseEntity<SseEmitter> subscribe(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        SseEmitter emitter = notificationService.subscribe(lastEventId);

        return ResponseEntity.ok(emitter);
    }

    @GetMapping(value = "/publish") // publish, consume 테스트 용 controller method
    @Operation(summary = "(임시) 알림 전송 테스트", description = "(임시) 알림 전송 테스트합니다.")
    @ApiResponse(responseCode = "200", description = "(임시) 알림 전송 테스트 성공")
    public ResponseEntity<?> publish() {
        publisher.publishEvent(NotificationEvent.builder()
                .receiverId(null)
                .message("publish test")
                .type(NotificationType.ANNOUNCEMENT)
                .build());

        return ResponseEntity.ok().build();
    }
}
