package com.umc.lifesharing.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import com.umc.lifesharing.login.dto.OIDCPublicKeyDto;
import com.umc.lifesharing.login.dto.OIDCPublicKeysResponse;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtOIDCUtil {

    @Autowired
    private final KakaoOauthClient kakaoOauthClient;

    public Jws<Claims> getKidFromUnsignedTokenClaims(String token, String iss, String aud) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
        Jwt jwt = getUnsignedTokenClaims(token, iss, aud);
        String kid = jwt.getHeader().get("kid").toString();

        OIDCPublicKeyDto key = kakaoOauthClient.getKakaoOIDCOpenKeys().getKeys()
                .stream()
                .filter(k -> k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.TOKEN_INVALID));

        return Jwts.parser()
                .setSigningKey(getRSAPublicKey(key.getN(), key.getE()))
                .parseClaimsJws(token);
    }

    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) throws JsonProcessingException {
        try {
            return Jwts.parser()
                    .requireAudience(aud)       //  aud 확인
                    .requireIssuer(iss)         //  issuer 확인
                    .parseClaimsJwt(getUnsignedToken(token));
        } catch (ExpiredJwtException e) {
            throw new GeneralException(ErrorStatus.TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error(e.toString());
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }
    }

    private PublicKey getRSAPublicKey(String modulus, String exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }

    private String getUnsignedToken(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        return splitToken[0] + "." + splitToken[1] + ".";
    }


}
