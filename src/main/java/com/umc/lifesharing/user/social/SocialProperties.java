package com.umc.lifesharing.user.social;

import com.umc.lifesharing.user.entity.enum_class.SocialType;

public interface SocialProperties {
    String getIss();
    String getAud();
    String getName();
    String getEmail();
    String getPicture();
    SocialType getSocialType();
}
