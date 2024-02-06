package com.umc.lifesharing.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.JwtProvider;
import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.Roles;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.entity.enum_class.SocialType;
import com.umc.lifesharing.user.repository.RolesRepository;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.social.GoogleProperties;
import com.umc.lifesharing.user.social.JwtOIDCProvider;
import com.umc.lifesharing.user.social.SocialProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OauthService {
    @Autowired
    private final GoogleProperties googleProperties;
    private final JwtOIDCProvider jwtOIDCProvider;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final JwtProvider jwtProvider;

    public UserResponseDTO.ResponseDTO socialLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        // 사용자가 있는지 검사 후 없으면 생성 있으면 로그인 처리
        User user = findOrCreateUser(idToken);
        Roles roles = new Roles().addUser(user);

        rolesRepository.save(roles);
        userRepository.save(user);

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
    }

    private Jws<Claims> getUserInfo2IdToken(String idToken, SocialProperties socialProperties) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return jwtOIDCProvider.getKidFromSignedTokenClaims(idToken, socialProperties.getIss(), socialProperties.getAud());
    }

    private User validUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
    }

    private User findOrCreateUser(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        Claims claims = getUserInfo2IdToken(idToken).getBody();
        return userRepository.findByEmailAndName(claims.get("email").toString(), claims.get("nickname").toString())
                .orElseGet(() -> createUser(claims));
    }

    private User createUser(Claims claims) {
        return User.builder()
                .email((String) claims.get("email"))
                .name((String) claims.get("nickname"))
                .profileUrl((String) claims.get("picture"))
                .socialType(SocialType.KAKAO)
                .build();
    }
}
