package com.umc.lifesharing.login;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao.keys")
@Data
public class KakaoPublicKeys {
    String kids1;
    String kty1;
    String alg1;
    String use1;
    String n1;
    String e1;

    String kids2;
    String kty2;
    String alg2;
    String use2;
    String n2;
    String e2;
}
