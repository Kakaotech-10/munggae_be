package com.ktb10.munggaebe.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.auth.dto.LoginResponse;
import com.ktb10.munggaebe.auth.exception.OAuthResponseJsonProcessingException;
import com.ktb10.munggaebe.auth.feign.KakaoAuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoAuthClient kakaoAuthClient;

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public LoginResponse login(String code) {

        String accessToken = getAccessToken(code);
        log.info("kakao accessToken = {}", accessToken);

        HashMap<String, Object> userInfo = getUserInfo(accessToken);

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

    private HashMap<String, Object> getUserInfo(String accessToken) {
        return null;
    }

    private LoginResponse loginWithInfo(HashMap<String, Object> userInfo) {
        return null;
    }

}
