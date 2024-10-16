package com.ktb10.munggaebe.auth.jwt;

import com.ktb10.munggaebe.auth.dto.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokens generate(String string) {
        return null;
    }
}
