package com.ktb10.munggaebe.notification.service.dto;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.notification.domain.Notification;
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

    public Notification toEntity(Member member) {
        return Notification.builder()
                .member(member)
                .type(type)
                .message(message)
                .build();
    }

    public Notification toEntity() {
        return Notification.builder()
                .type(type)
                .message(message)
                .build();
    }
}
