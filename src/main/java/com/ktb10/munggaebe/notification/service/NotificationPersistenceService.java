package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.notification.repository.NotificationRepository;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        notificationRepository.save(event.toEntity());
    }

    @Transactional
    public void saveUnicasting(NotificationEvent event) {
        log.info("saveUnicasting start: receiverId = {}", event.getReceiverId());
        Member member = findMemberByReceiverId(event.getReceiverId());
        notificationRepository.save(event.toEntity(member));
    }

    public Member findMemberByReceiverId(long receiverId) {
        return memberRepository.findById(receiverId)
                .orElseThrow(() -> new MemberNotFoundException(receiverId));
    }
}
