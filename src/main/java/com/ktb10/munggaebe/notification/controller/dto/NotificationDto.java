package com.ktb10.munggaebe.notification.controller.dto;

import com.ktb10.munggaebe.notification.domain.Notification;
import com.ktb10.munggaebe.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

public class NotificationDto {

    @Schema(description = "알림 응답")
    @Getter
    public static class NotificationRes {
        @Schema(description = "알림 ID", example = "1")
        private Long id;

        @Schema(description = "알림 종류", example = "ANNOUNCEMENT")
        private NotificationType type;

        @Schema(description = "알림 메세지", example = "이런저런 공지사항입니다.")
        private String message;

        @Schema(description = "알림 읽음 여부", example = "false")
        private boolean isRead;

        @Schema(description = "알림 생성 시간", example = "2024-10-15T10:15:30")
        private LocalDateTime createdAt;

        @Schema(description = "알림 수정 시간", example = "2024-10-15T10:15:30")
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
