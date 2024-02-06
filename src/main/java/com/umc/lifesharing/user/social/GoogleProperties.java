package com.umc.lifesharing.user.social;

import com.umc.lifesharing.user.entity.enum_class.SocialType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google")
@Data
public class GoogleProperties implements SocialProperties {
    String restApiKey;
    String iss;
    String aud;
    SocialType socialType;
}
