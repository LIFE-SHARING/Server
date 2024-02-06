package com.umc.lifesharing.user.social.kakao;

import com.umc.lifesharing.user.entity.enum_class.SocialType;
import com.umc.lifesharing.user.social.SocialProperties;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "kakao")
@Data
@Getter
public class KakaoProperties implements SocialProperties {
    String restApiKey;
    String iss;
    String aud;
    String name = "nickname";
    String email = "email";
    String picture = "picture";
    SocialType socialType = SocialType.GOOGLE;
}
