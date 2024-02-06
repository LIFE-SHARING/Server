package com.umc.lifesharing.user.social;

import com.umc.lifesharing.user.entity.enum_class.SocialType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Data
public class KakaoProperties implements SocialProperties {
    String restApiKey;
    String iss;
    String aud;
    SocialType socialType;
}
