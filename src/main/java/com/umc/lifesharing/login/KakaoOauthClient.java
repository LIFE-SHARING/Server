package com.umc.lifesharing.login;

import com.umc.lifesharing.login.dto.OIDCPublicKeysResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoAuthClient",
        url = "https://kauth.kakao.com")
public interface KakaoOauthClient {
    @Cacheable(cacheNames = "KakaoOICD", cacheManager = "cacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeysResponse getKakaoOIDCOpenKeys();
}