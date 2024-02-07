package com.umc.lifesharing.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.config.security.JwtProvider;
import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.Roles;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.helper.LoginHelper;
import com.umc.lifesharing.user.repository.RolesRepository;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.social.*;
import com.umc.lifesharing.user.social.google.GoogleOauthClient;
import com.umc.lifesharing.user.social.google.GoogleProperties;
import com.umc.lifesharing.user.social.kakao.KakaoOauthClient;
import com.umc.lifesharing.user.social.kakao.KakaoProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OauthService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    private final JwtProvider jwtProvider;
    private final LoginHelper loginHelper;

    private final KakaoProperties kakaoProperties;
    private final GoogleProperties googleProperties;

    private final KakaoOauthClient kakaoOauthClient;
    private final GoogleOauthClient googleOauthClient;

    public UserResponseDTO.ResponseDTO kakaoLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return getResponseDTO(idToken, kakaoProperties, kakaoOauthClient);
    }

    public UserResponseDTO.ResponseDTO googleLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return getResponseDTO(idToken, googleProperties, googleOauthClient);
    }

//    public UserResponseDTO.ResponseDTO naverLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
//        return getResponseDTO(idToken, socialProperties);
//    }

    private UserResponseDTO.ResponseDTO getResponseDTO(String idToken, SocialProperties socialProperties, OauthClient oauthClient)
            throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        // 사용자가 있는지 검사 후 없으면 생성 있으면 로그인 처리
        User user = loginHelper.findOrCreateUser(idToken, socialProperties, oauthClient);
        user.setTemporaryNickname();    // 소셜 로그인시 받아오는 name이 nickname이 아닌 경우가 있어서 임시 닉네임으로 설정 후 저장
        Roles roles = new Roles().addUser(user);

        rolesRepository.save(roles);
        userRepository.save(user);

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
    }


}
