package com.umc.lifesharing.login.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.login.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/kakao")
    public ApiResponse<HashMap<String, Object>> kakaoLogin(@RequestParam(name = "code") String code) {
//        String token = oauthService.getAccessToken(code);
//        return ApiResponse.onSuccess(oauthService.getUserInfo(token));
        return null;
    }
}
