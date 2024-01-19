package com.umc.lifesharing.login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.HashMap;

public interface OauthService {
    String getAccessToken(String code);

//    public Jws<Claims> getUserInfo2IdTkn(String idToken)


}
