package com.ktb10.munggaebe.notification.domain;

public enum NotificationType {

    ANNOUNCEMENT("공지사항 알림"),
    ADD_ROOT_COMMENT("게시글에 댓글 달림"),
    ADD_REPLY_COMMENT("댓글에 대댓글 달림"),
    MENTION("멘션 알림")
    ;

    private String name;

    NotificationType(String name) {
        this.name = name;
    }
}
