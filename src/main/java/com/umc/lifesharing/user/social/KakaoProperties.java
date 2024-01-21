package com.umc.lifesharing.user.social;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Data
public class KakaoProperties {
    String restApiKey;
    String iss;
    String aud;
}
