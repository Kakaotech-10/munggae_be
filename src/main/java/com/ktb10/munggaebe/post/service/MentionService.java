package com.ktb10.munggaebe.post.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.service.MemberService;
import com.ktb10.munggaebe.notification.domain.NotificationType;
import com.ktb10.munggaebe.notification.service.NotificationEventPublisher;
import com.ktb10.munggaebe.notification.service.dto.NotificationEvent;
import com.ktb10.munggaebe.post.service.dto.MemberSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentionService {

    private final ElasticsearchMemberSearchService elasticsearchMemberSearchService;
    private final NotificationEventPublisher notificationEventPublisher;
    private final MemberService memberService;

    private static final String INDEX_NAME = "members";

    public List<String> searchMemberName(String keyword) {
        log.info("searchMemberName start : keyword = {}", keyword);
        List<MemberSearchDto> result = elasticsearchMemberSearchService.search(INDEX_NAME, keyword);
        log.info("searchMemberName result = {}", result);
        return result.stream()
                .map(MentionService::toFullName)
                .limit(5L)
                .toList();
    }

    private static String toFullName(MemberSearchDto dto) {
        return dto.getAlias() + "(" + dto.getName() + ")";
    }

    public void sendNotification(String name) {

        if (name.equals("everyone")) {
            //전체 전송
            return;
        }

        Member member = memberService.findMemberByFullName(name);
        Long receiverId = member.getId();

        NotificationEvent notificationEvent = notificationEventPublisher.createUniCastingEvent(receiverId, NotificationType.MENTION, "멘션 되었습니다.");
        notificationEventPublisher.publishEvent(notificationEvent);
    }
}
