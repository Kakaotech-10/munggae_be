package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.exception.MemberPermissionDeniedException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.notification.domain.Notification;
import com.ktb10.munggaebe.notification.repository.NotificationRepository;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import com.ktb10.munggaebe.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveBroadCasting(NotificationEvent event) {
        log.info("saveBroadCasting start");
        List<Member> members = memberRepository.findAll();
        members.stream()
                .filter(m -> m.getRole().equals(MemberRole.STUDENT))
                .forEach(member -> notificationRepository.save(event.toEntity(member)));

        return notificationRepository.findLastId();
    }

    @Transactional
    public Long saveUnicasting(NotificationEvent event) {
        log.info("saveUnicasting start: receiverId = {}", event.getReceiverId());
        Member member = findMemberByReceiverId(event.getReceiverId());
        Notification savedNotification = notificationRepository.save(event.toEntity(member));
        return savedNotification.getId();
    }

    @Transactional
    public Notification markNotificationAsRead(long notificationId) {
        log.info("markNotificationAsRead start: notificationId = {}", notificationId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 알림입니다. notificationId = " + notificationId));

        validateAuthorization(notification);

        notification.markAsRead();

        return notification;
    }

    public Page<Notification> findAllMyNotifications(int pageNo, int pageSize) {
        log.info("findAllMyNotifications start");

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Long memberId = SecurityUtil.getCurrentUserId();

        return notificationRepository.findAllByMemberId(pageable, memberId);
    }

    private Member findMemberByReceiverId(long receiverId) {
        return memberRepository.findById(receiverId)
                .orElseThrow(() -> new MemberNotFoundException(receiverId));
    }

    private void validateAuthorization(Notification notification) {
        log.info("validateAuthorization Notification's memberId");
        Long currentMemberId = SecurityUtil.getCurrentUserId();
        if (SecurityUtil.hasRole("STUDENT") && !notification.getMember().getId().equals(currentMemberId)) {
            throw new MemberPermissionDeniedException(currentMemberId, MemberRole.STUDENT);
        }
    }

    public List<Notification> getMissingNotifications(Long userId, String lastEventId) {
        long lastEventIdAsLong;
        try {
            lastEventIdAsLong = Long.parseLong(lastEventId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("잘못된 형식의 lastEventId입니다. lastEventId = " + lastEventId);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

        return notificationRepository.findByMemberIdAndIdGreaterThan(userId, lastEventIdAsLong, sort);
    }
}
