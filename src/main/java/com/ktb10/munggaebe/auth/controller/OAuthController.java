package com.ktb10.munggaebe.auth.controller;

import com.ktb10.munggaebe.auth.dto.LoginResponse;
import com.ktb10.munggaebe.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class OAuthController {

    private final KakaoService kakaoService;

    @ResponseBody
    @GetMapping("/login/oauth2/callback/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam String code) {
        return ResponseEntity.ok(kakaoService.login(code));
    }
}
