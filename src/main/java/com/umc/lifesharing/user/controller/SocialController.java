package com.umc.lifesharing.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
@Slf4j
public class SocialController {
    private final SocialService socialService;

    @PostMapping("/kakao/join")
    public ApiResponse<UserResponseDTO.ResponseDTO> kakaoJoin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(socialService.socialJoin(idToken));
    }

    @PostMapping("/kakao/login")
    public ApiResponse<UserResponseDTO.ResponseDTO> kakaoLogin(@RequestParam(name = "idToken") String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(socialService.socialLogin(idToken));
    }

}
