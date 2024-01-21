package com.umc.lifesharing.login;

import com.umc.lifesharing.user.controller.SocialController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SocialLoginTests {
    @Autowired
    private SocialController socialController;

    @Test
    public void kakaoLoginTest(){

    }
}
