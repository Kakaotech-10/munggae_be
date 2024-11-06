package com.ktb10.munggaebe.auth.jwt;

import com.ktb10.munggaebe.auth.service.dto.AccessTokenResponse;
import com.ktb10.munggaebe.auth.service.dto.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;	           //1hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;    //14days

    public AccessTokenResponse generateAccessToken(String uid, Collection<? extends GrantedAuthority> authorities) {
        log.info("TokenManager.generateAccessToken start");
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = jwtTokenProvider.accessTokenGenerate(uid, authorities, accessTokenExpiredAt);

        return AccessTokenResponse.of(accessToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }

    public RefreshTokenResponse generateRefreshToken() {
        long now = (new Date()).getTime();
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String refreshToken = jwtTokenProvider.refreshTokenGenerate(refreshTokenExpiredAt);

        return RefreshTokenResponse.of(refreshToken, REFRESH_TOKEN_EXPIRE_TIME / 1000L);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
