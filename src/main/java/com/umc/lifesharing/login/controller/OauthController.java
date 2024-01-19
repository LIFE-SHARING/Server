package com.umc.lifesharing.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.login.JwtOIDCUtil;
import com.umc.lifesharing.login.service.KakaoService;
import com.umc.lifesharing.login.service.OauthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final JwtOIDCUtil jwtOIDCUtil;
    private final KakaoService kakaoService;

    @GetMapping("/kakao/login")
    public ApiResponse<Jws<Claims>> kakaoLogin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(kakaoService.getUserInfo2IdTkn(idToken));
    }


}
