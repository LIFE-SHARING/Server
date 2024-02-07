package com.umc.lifesharing.user.social.kakao;

import com.umc.lifesharing.user.dto.OIDCPublicKeysResponse;
import com.umc.lifesharing.user.social.OauthClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoAuthClient",
        url = "https://kauth.kakao.com")
public interface KakaoOauthClient extends OauthClient {
    @Cacheable(cacheNames = "KakaoOIDC", cacheManager = "cacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeysResponse getOIDCOpenKeys();
}