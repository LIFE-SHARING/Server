package com.umc.lifesharing.user.dto;

import com.umc.lifesharing.user.entity.enum_class.SocialType;

public class KakaoResposeDTO implements OauthResponse {

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getNickname() {
        return null;
    }

    @Override
    public SocialType getSocialType() {
        return null;
    }
}
