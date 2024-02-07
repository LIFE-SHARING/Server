package com.umc.lifesharing.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
@Slf4j
public class SocialController {
    private final OauthService oauthService;

    @PostMapping("/kakao/login")
    public ApiResponse<UserResponseDTO.ResponseDTO> kakaoOIDCLogin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(oauthService.kakaoLogin(idToken));
    }

    @PostMapping("/google/login")
    public ApiResponse<UserResponseDTO.ResponseDTO> googleOIDCLogin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(oauthService.googleLogin(idToken));
    }

    @PostMapping("/naver/login")
    public ApiResponse<UserResponseDTO.ResponseDTO> naveOauthLogin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
//        return ApiResponse.onSuccess(oauthService.naverLogin(idToken, naverProperties));
        return null;
    }

}
