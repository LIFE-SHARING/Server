package com.umc.lifesharing.login;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Helper;

//@Helper
//@RequiredArgsConstructor
//public class OauthOIDCHelper {
//    private final JwtOIDCUtil jwtOIDCUtile;
//    private String getKidFromUnsignedIdToken(String token, String iss, String aud) {
//        return jwtOIDCUtile.getKidFromUnsignedTokenHeader(token, iss, aud);
//    }
//
//    public OIDCDecodePayload getPayloadFromIdToken(String token, String iss, String aud, OIDCPublicKeysResponse oidcPublicKeysResponse) {
//        String kid = getKidFromUnsignedIdToken(token, iss, aud);
//        // KakaoOauthHelper 에서 공개키를 조회했고 해당 디티오를 넘겨준다.
//        OIDCPublicKeysResponse.Keys keys = oidcPublicKeysResponse.getKeys().stream()
//                        .filter(o -> o.getKid().equals(kid))
//                        .findFirst()
//                        .orElseThrow();
//        // 검증이 된 토큰에서 바디를 꺼내온다.
//        return jwtOIDCUtile.getOIDCTokenBody(
//                token, oidcPublicKeyDto.getN(), oidcPublicKeyDto.getE());
//    }
//}