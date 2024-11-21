package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.exception.MemberNotFoundException;
import com.ktb10.munggaebe.member.repository.MemberRepository;
import com.ktb10.munggaebe.notification.repository.NotificationRepository;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveBroadCasting(NotificationEvent event) {
        notificationRepository.save(event.toEntity());
    }

    @Transactional
    public void saveUnicasting(NotificationEvent event) {
        Member member = findMemberByReceiverId(event.getReceiverId());
        notificationRepository.save(event.toEntity(member));
    }

    public Member findMemberByReceiverId(long receiverId) {
        return memberRepository.findById(receiverId)
                .orElseThrow(() -> new MemberNotFoundException(receiverId));
    }
}
