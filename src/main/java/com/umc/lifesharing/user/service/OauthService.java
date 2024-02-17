package com.umc.lifesharing.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;

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

    public UserResponseDTO.ResponseDTO kakaoLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return getResponseDTO(idToken, kakaoProperties, kakaoOauthClient);
    }

    public UserResponseDTO.ResponseDTO googleLogin(String idToken) throws IOException, FirebaseAuthException {
        log.info("googleLogin");
        FirebaseToken signedToken;
        FileInputStream config = new FileInputStream("service-account-file.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(config))
                .setProjectId(googleProperties.getAud())
                .build();

        if(FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        } else {
            FirebaseApp.getApps();
        }

        try {
            signedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            throw new GeneralException(ErrorStatus.ID_TOKEN_UNSUPPORTED);
        }

        User user = userRepository.findByEmail(signedToken.getEmail())
                .orElseGet(() -> User.builder()
                        .email(signedToken.getEmail())
                        .name(signedToken.getName())
                        .socialType(googleProperties.getSocialType())
                        .build());

        user.setTemporaryNickname();
        Roles roles = new Roles().addUser(user);

        rolesRepository.save(roles);
        userRepository.save(user);

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
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
