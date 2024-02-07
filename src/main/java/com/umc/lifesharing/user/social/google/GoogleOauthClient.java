package com.umc.lifesharing.user.social.google;

import com.umc.lifesharing.user.dto.OIDCPublicKeysResponse;
import com.umc.lifesharing.user.social.OauthClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "GoogleAuthClient",
        url = "https://identitytoolkit.googleapis.com")
public interface GoogleOauthClient extends OauthClient {
    @Cacheable(cacheNames = "GoogleOIDC", cacheManager = "cacheManager")
    @GetMapping("/v1/sessionCookiePublicKeys")
    OIDCPublicKeysResponse getOIDCOpenKeys();
}