package com.umc.lifesharing.login.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.JwtUtil;
import com.umc.lifesharing.login.JwtOidcUtil;
import com.umc.lifesharing.login.service.OauthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final OauthService oauthService;
    private final JwtOidcUtil jwtOidcUtil;

//    @GetMapping("/kakao/login")
//    public ApiResponse<Claims> kakaoLogin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException {

//        String token = oauthService.getAccessToken(code);
//        return ApiResponse.onSuccess(oauthService.getUserInfo(token));
//        return null;
//    }


}
