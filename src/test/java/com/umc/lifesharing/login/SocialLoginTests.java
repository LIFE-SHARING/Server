package com.umc.lifesharing.login;

import com.umc.lifesharing.login.controller.OauthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SocialLoginTests {
    @Autowired
    private OauthController oauthController;

    @Test
    public void kakaoLoginTest(){

    }
}
