package com.umc.lifesharing.login.service;

import java.util.HashMap;

public interface OauthService {
    String getAccessToken(String code);

    HashMap<String, Object> getUserInfo2accessTkn(String accessToken);


}
