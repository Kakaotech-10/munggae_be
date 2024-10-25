package com.ktb10.munggaebe.auth.controller;

import com.ktb10.munggaebe.auth.dto.AccessTokenResponse;
import com.ktb10.munggaebe.auth.dto.LoginResponse;
import com.ktb10.munggaebe.auth.dto.RefreshTokenResponse;
import com.ktb10.munggaebe.auth.service.KakaoService;
import com.ktb10.munggaebe.auth.service.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.SET_COOKIE;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class OAuthController {

    private static final String COOKIE_TOKEN = "refresh-token";

    private final KakaoService kakaoService;

    @GetMapping("/login/oauth2/callback/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestParam final String code, final HttpServletResponse response) {
        log.info("kakaoLogin start");
        final LoginDto dto = kakaoService.login(code);

        final RefreshTokenResponse refreshTokenResponse = dto.getRefreshToken();
        if (refreshTokenResponse != null) {
            log.info("refreshToken on cookie");
            final ResponseCookie cookie = ResponseCookie.from(COOKIE_TOKEN, refreshTokenResponse.getRefreshToken())
                    .maxAge(refreshTokenResponse.getExpiresIn())
                    .sameSite("None")
                    .secure(true)
                    .httpOnly(true)
                    .path("/")
                    .build();

            response.addHeader(SET_COOKIE, cookie.toString());
        }

        return ResponseEntity.ok(new LoginResponse(dto.getId(), dto.getNickname(), dto.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> regenerateAccessToken(
            @CookieValue(COOKIE_TOKEN) final String refreshToken,
            final HttpServletRequest request
    ) {

        log.info("regenerateAccessToken start");
        final AccessTokenResponse regeneratedAccessToken = kakaoService.regenerateAccessToken(refreshToken, request);
        log.info("regenerateAccessToken = {}", regeneratedAccessToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(regeneratedAccessToken);
    }
}
