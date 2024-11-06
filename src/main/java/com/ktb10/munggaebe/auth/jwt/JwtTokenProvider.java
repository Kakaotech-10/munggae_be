package com.ktb10.munggaebe.auth.jwt;

import com.ktb10.munggaebe.auth.exception.*;
import com.ktb10.munggaebe.member.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final MemberService memberService;
    private Key key;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String secretKey, MemberService memberService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberService = memberService;
    }

    public String accessTokenGenerate(String subject, Collection<? extends GrantedAuthority> authorities, Date expiredAt) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("auth", authorities)
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String refreshTokenGenerate(Date expiredAt) {
        return Jwts.builder()
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw new InvalidSignatureCodeException();
        } catch (SecurityException e) {
            throw new SecurityCodeException();
        } catch (MalformedJwtException e) {
            throw new MalformedTokenCodeException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenCodeException();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedTokenCodeException();
        } catch (IllegalArgumentException e) {
            throw new IllegalTokenCodeException();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        UserDetails userDetails = memberService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
