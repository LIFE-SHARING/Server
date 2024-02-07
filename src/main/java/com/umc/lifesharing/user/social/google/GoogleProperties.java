package com.umc.lifesharing.user.social.google;

import com.umc.lifesharing.user.entity.enum_class.SocialType;
import com.umc.lifesharing.user.social.SocialProperties;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google")
@Data
@Getter
public class GoogleProperties implements SocialProperties {
    String restApiKey;
    String iss;
    String aud;
    String name = "name";
    String email = "email";
    String picture = "picture";
    SocialType socialType = SocialType.GOOGLE;
}
