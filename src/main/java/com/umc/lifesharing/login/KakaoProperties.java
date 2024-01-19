package com.umc.lifesharing.login;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth.oidc.kakao")
@Data
public class KakaoProperties {
    String restApiKey;
    String iss;
    String aud;
}
