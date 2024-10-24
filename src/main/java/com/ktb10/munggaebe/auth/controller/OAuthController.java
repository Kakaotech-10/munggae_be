package com.ktb10.munggaebe.auth.controller;

import com.ktb10.munggaebe.auth.dto.LoginResponse;
import com.ktb10.munggaebe.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class OAuthController {

    private final KakaoService kakaoService;

    @GetMapping("/login/oauth2/callback/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {
        log.info("kakaoLogin start");
        return ResponseEntity.ok(kakaoService.login(code));
    }
}
