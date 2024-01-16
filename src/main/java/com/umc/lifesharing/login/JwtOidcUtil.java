package com.umc.lifesharing.login;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
public class JwtOidcUtil {

    @Builder
    public static class Jwt {
        String header;
        String payload;
        String signature;
    }


    public Jwt getUnsignedToken(String token) {
        String[] splitToken = token.split("\\.");

        if (splitToken.length != 3)
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);

        return Jwt.builder()
                .header(splitToken[0])
                .payload(splitToken[1])
                .signature(splitToken[2])
                .build();
    }

//    public void validToken(String token) {
//        Jwt jwt = getUnsignedToken(token);
//        Claims claims = Jwts.parser()
//                .setSigningKey(getRSAPublicKey(n, e))
//                .parseClaimsJws(token)
//                .getBody();
//    }

    private PublicKey getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }
}
