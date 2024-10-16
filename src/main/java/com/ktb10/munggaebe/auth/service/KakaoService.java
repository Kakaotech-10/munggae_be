package com.ktb10.munggaebe.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.auth.dto.LoginResponse;
import com.ktb10.munggaebe.auth.exception.OAuthResponseJsonProcessingException;
import com.ktb10.munggaebe.auth.feign.KakaoAuthClient;
import com.ktb10.munggaebe.auth.feign.KakaoMemberApiClient;
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
    private final KakaoMemberApiClient kakaoMemberApiClient;

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public LoginResponse login(String code) {

        log.info("getAccessToken start");
        String accessToken = getAccessToken(code);
        log.info("kakao accessToken = {}", accessToken);

        log.info("getMemberInfo start");
        HashMap<String, Object> userInfo = getMemberInfo(accessToken);

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
        log.info("kakao responseBody = {}", responseBody);

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

    private LoginResponse loginWithInfo(HashMap<String, Object> memberInfo) {
        return null;
    }

}
