package com.umc.lifesharing.user.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import com.umc.lifesharing.user.dto.OIDCPublicKeyDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JwtOIDCProvider {

    public Jws<Claims> getKidFromSignedTokenClaims(String token, String iss, String aud, OauthClient OauthClient) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
        if(iss == null || aud == null)
            throw new NullPointerException();       // TODO: 핸들러에게 넘기기

        Jwt jwt = getUnsignedTokenClaims(token, iss, aud);
        String kid = jwt.getHeader().get("kid").toString();

        OIDCPublicKeyDto key = OauthClient.getOIDCOpenKeys().getKeys()
                .stream()
                .filter(k -> k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.TOKEN_INVALID));

        return Jwts.parserBuilder()
                .setSigningKey(getRSAPublicKey(key.getN(), key.getE()))
                .build()
                .parseClaimsJws(token);
    }

    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) throws JsonProcessingException {
        try {
            return Jwts.parserBuilder()
                    .requireAudience(aud)       //  aud 확인
                    .requireIssuer(iss)         //  issuer 확인
                    .build()
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
