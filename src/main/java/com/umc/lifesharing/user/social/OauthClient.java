package com.umc.lifesharing.user.social;

import com.umc.lifesharing.user.dto.OIDCPublicKeysResponse;

public interface OauthClient {
    OIDCPublicKeysResponse getOIDCOpenKeys();
}
