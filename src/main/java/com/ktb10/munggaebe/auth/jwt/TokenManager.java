package com.ktb10.munggaebe.auth.jwt;

import com.ktb10.munggaebe.auth.dto.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;	           //1hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;    //14days

    public AuthTokens generate(String uid) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = jwtTokenProvider.accessTokenGenerate(uid, accessTokenExpiredAt);
        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiredAt);

        return AuthTokens.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }
}
