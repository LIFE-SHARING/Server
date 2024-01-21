package com.umc.lifesharing.user.dto;

import com.umc.lifesharing.user.entity.enum_class.SocialType;

public interface OauthResponse {
    String getEmail();
    String getNickname();
    SocialType getSocialType();
}
