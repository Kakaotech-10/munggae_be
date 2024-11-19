package com.ktb10.munggaebe.notification.service;

import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.domain.MemberRole;
import com.ktb10.munggaebe.notification.domain.SseEmitterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private SseEmitterRepository emitterRepository;

    @BeforeEach
    void setup() {
        Member member = Member.builder().id(1L).role(MemberRole.STUDENT).build();
        setupSecurityContextWithRole(member, "STUDENT");
    }

    private void setupSecurityContextWithRole(Member member, String role) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member, null, Collections.singleton(() -> "ROLE_" + role)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("구독한 사용자의 emitter를 성공적으로 저장한다.")
    void subscribe_success() {
        // Given
        long lastEventId = 1L;
        SseEmitter emitter = new SseEmitter(1L);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(emitter);
    }

    @Test
    @DisplayName("생성한 emitter의 만료시간은 정해진 시간이다.")
    void subscribe_timeout() {
        // Given
        long lastEventId = 1L;
        long defaultTimeout = 1L;
        SseEmitter emitter = new SseEmitter(defaultTimeout);

        given(emitterRepository.save(anyLong(), any())).willReturn(emitter);

        // When
        SseEmitter result = notificationService.subscribe(lastEventId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTimeout()).isEqualTo(defaultTimeout);
    }
}