package com.ktb10.munggaebe.auth.service;

import com.ktb10.munggaebe.auth.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class KakaoService {

    @Value("${kakao.key.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public LoginResponse login(String code) {

        String accessToken = getAccessToken(code);
        HashMap<String, Object> userInfo = getUserInfo(accessToken);

        return loginWithInfo(userInfo);
    }

    private String getAccessToken(String code) {
        return null;
    }

    private HashMap<String, Object> getUserInfo(String accessToken) {
        return null;
    }

    private LoginResponse loginWithInfo(HashMap<String, Object> userInfo) {
        return null;
    }

}
