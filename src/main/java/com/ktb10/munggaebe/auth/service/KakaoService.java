package com.ktb10.munggaebe.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.auth.dto.AccessTokenResponse;
import com.ktb10.munggaebe.auth.dto.RefreshTokenResponse;
import com.ktb10.munggaebe.auth.exception.OAuthResponseJsonProcessingException;
import com.ktb10.munggaebe.auth.feign.KakaoAuthClient;
import com.ktb10.munggaebe.auth.feign.KakaoMemberApiClient;
import com.ktb10.munggaebe.auth.jwt.JwtTokenProvider;
import com.ktb10.munggaebe.auth.jwt.TokenManager;
import com.ktb10.munggaebe.auth.service.dto.LoginDto;
import com.ktb10.munggaebe.member.domain.Member;
import com.ktb10.munggaebe.member.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoMemberApiClient kakaoMemberApiClient;
    private final TokenManager tokenManager;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public LoginDto login(String code) {

        log.info("getAccessToken start");
        String accessToken = getAccessToken(code);
        log.info("kakao accessToken = {}", accessToken);

        log.info("getMemberInfo start");
        HashMap<String, Object> userInfo = getMemberInfo(accessToken);

        log.info("loginWithInfo start");
        return loginWithInfo(userInfo);
    }

    private String getAccessToken(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        String grantType = "authorization_code";

        ResponseEntity<String> response = kakaoAuthClient.getKakaoToken(contentType, grantType, clientId, redirectUri, code);
        log.info("kakao response = {}", response);

        String responseBody = response.getBody();
        log.info("kakao responseBody = {}", responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new OAuthResponseJsonProcessingException(responseBody);
        }
        log.info("kakao jsonNode = {}", jsonNode);

        return jsonNode.get("access_token").asText();
    }

    private HashMap<String, Object> getMemberInfo(String accessToken) {

        HashMap<String, Object> memberInfo= new HashMap<>();

        String authorization = "Bearer " + accessToken;
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";

        ResponseEntity<String> response = kakaoMemberApiClient.getKakaoMember(authorization, contentType);
        log.info("kakao response = {}", response);

        String responseBody = response.getBody();
//        log.info("kakao responseBody = {}", responseBody);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new OAuthResponseJsonProcessingException(responseBody);
        }
        log.info("kakao jsonNode = {}", jsonNode);

        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        log.info("kakao id = {}", id);
        log.info("kakao nickname = {}", nickname);

        memberInfo.put("id",id);
        memberInfo.put("nickname",nickname);

        return memberInfo;
    }

    private LoginDto loginWithInfo(HashMap<String, Object> memberInfo) {
        Long kakaoId = Long.valueOf(memberInfo.get("id").toString());
        String nickName = memberInfo.get("nickname").toString();

        Member member = memberService.joinKakao(kakaoId, nickName);

        AccessTokenResponse accessTokenResponse = tokenManager.generateAccessToken(kakaoId.toString(), member.getAuthorities());
        RefreshTokenResponse refreshTokenResponse = tokenManager.generateRefreshToken();
        log.info("발급된 AccessToken = {}", accessTokenResponse);
        log.info("발급된 RefreshToken = {}", refreshTokenResponse);

        return new LoginDto(member.getId(), kakaoId, nickName, accessTokenResponse, refreshTokenResponse);
    }

    public AccessTokenResponse regenerateAccessToken(String refreshToken, HttpServletRequest request) {

        String accessToken = tokenManager.resolveToken(request);

        if (accessToken == null) {
            throw new RuntimeException("잘못된 access token입니다.");
        }

        log.info("refreshToken = {}", refreshToken);
        log.info("refreshToken valid 검사");
        if (jwtTokenProvider.validateToken(refreshToken)) {

            log.info("validate refreshToken = {}", refreshToken);
            Claims claims = jwtTokenProvider.parseClaims(accessToken);
            String kakaoId = claims.getSubject();

            UserDetails userDetails = memberService.loadUserByUsername(kakaoId);
            log.info("userDetails = {}",userDetails);

            return tokenManager.generateAccessToken(kakaoId, userDetails.getAuthorities());
        }

        log.info("refreshToken inValid = {}", refreshToken);
        throw new RuntimeException("refresh token이 유효하지 않습니다.");
    }
}
