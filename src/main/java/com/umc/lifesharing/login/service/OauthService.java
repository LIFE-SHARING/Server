package com.umc.lifesharing.login.service;

import java.util.HashMap;

public interface OauthService {
    public String getAccessToken(String code);

    public HashMap<String, Object> getUserInfo(String accessToken);
}
