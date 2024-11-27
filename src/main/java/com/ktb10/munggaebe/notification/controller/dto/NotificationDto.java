package com.ktb10.munggaebe.notification.controller.dto;

import com.ktb10.munggaebe.notification.domain.Notification;
import com.ktb10.munggaebe.notification.domain.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationDto {

    @Getter
    public static class NotificationRes {
        private Long id;
        private NotificationType type;
        private String message;
        private boolean isRead;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public NotificationRes(Notification notification) {
            this.id = notification.getId();
            this.type = notification.getType();
            this.message = notification.getMessage();
            this.isRead = notification.isRead();
            this.createdAt = notification.getCreatedAt();
            this.updatedAt = notification.getUpdatedAt();
        }
    }
}
