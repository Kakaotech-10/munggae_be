package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveBroadCasting(NotificationEvent event) {
        log.info("saveBroadCasting start");
        List<Member> members = memberRepository.findAll();
        members.stream()
                .filter(m -> m.getRole().equals(MemberRole.STUDENT))
                .forEach(member -> notificationRepository.save(event.toEntity(member)));
    }

    @Transactional
    public void saveUnicasting(NotificationEvent event) {
        log.info("saveUnicasting start: receiverId = {}", event.getReceiverId());
        Member member = findMemberByReceiverId(event.getReceiverId());
        notificationRepository.save(event.toEntity(member));
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
}
