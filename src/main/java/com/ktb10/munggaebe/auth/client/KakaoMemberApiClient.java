package com.ktb10.munggaebe.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoMemberApiClient", url = "https://kapi.kakao.com/v2/user")
public interface KakaoMemberApiClient {

    @PostMapping(value = "/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> getKakaoMember(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Content-type") String contentType
    );
}
