package com.umc.lifesharing.config.security;

import com.umc.lifesharing.user.service.UserQueryService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.umc.lifesharing.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;

/**
 * JwtProvider 토큰 생성, 토큰 복호화 및 정보 추출, 토큰 유효성 검증의 기능이 구현된 클래스.
 * @author rimsong
 * application.properties에 jwt.secret: 값을 넣어 설정 추가해준 뒤 사용합니다.
 * jwt.secret는 토큰의 암호화, 복호화를 위한 secret key로서 이후 HS256알고리즘을 사용하기 위해, 256비트보다 커야합니다.
 * 알파벳이 한 단어당 8bit니, 32글자 이상이면 됩니다! 너무 짧으면 에러가 뜹니다.
 */
@Component
@Slf4j
public class JwtProvider {
    private final Key key;
    private final UserQueryService userQueryService;

    public JwtProvider(@Value("${jwt.secret}") String secretKey, UserQueryService userQueryService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userQueryService = userQueryService;
    }

    public TokenDTO generateTokenByUser(User user) {
        // 권한 가져오기
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("roles", user.getRoles());
        claims.put("nickname", user.getName());

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
//                .setIssuer()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + 1000 * 60 * 30))   //  30분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + 1000 * 60 * 60 * 24 * 7))  // 7일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserDetails getUserDetailsFromToken(String accessToken) {
        String email = String.valueOf(getClaims(accessToken).get("sub"));
        return userQueryService.loadUserByUsername(email);
    }

    /*
     * 토큰의 Claim 디코딩
     */
    private Claims getClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
}
