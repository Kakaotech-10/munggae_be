package com.ktb10.munggaebe.notification.service.dto;

import com.ktb10.munggaebe.notification.domain.NotificationType;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEvent {

    private Long receiverId;
    private NotificationType type;
    private String message;

    @Builder
    public NotificationEvent(Long receiverId, NotificationType type, String message) {
        this.receiverId = receiverId;
        this.type = type;
        this.message = message;
    }
}
