package com.umc.lifesharing.user.social.google;

import com.umc.lifesharing.user.dto.OIDCPublicKeysResponse;
import com.umc.lifesharing.user.social.OauthClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(
        name = "GoogleAuthClient",
        url = "https://www.googleapis.com")
@Deprecated
public interface GoogleOauthClient {
    @Cacheable(cacheNames = "GoogleOIDC", cacheManager = "cacheManager")
    @GetMapping("/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
    Map<String, String> getOIDCOpenKeys();
}