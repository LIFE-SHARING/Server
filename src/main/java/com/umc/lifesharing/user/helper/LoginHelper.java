package com.umc.lifesharing.user.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.social.JwtOIDCProvider;
import com.umc.lifesharing.user.social.OauthClient;
import com.umc.lifesharing.user.social.SocialProperties;
import com.umc.lifesharing.user.social.google.GoogleOauthClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoginHelper {
    private final JwtOIDCProvider jwtOIDCProvider;
    private final UserRepository userRepository;

    private Jws<Claims> getUserInfo2IdToken(String idToken, SocialProperties socialProperties, OauthClient oauthClient) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return jwtOIDCProvider.getKidFromSignedTokenClaims(idToken, socialProperties.getIss(), socialProperties.getAud(), oauthClient);
    }

    public User validUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
    }

    public User findOrCreateUser(String idToken, SocialProperties socialProperties, OauthClient oauthClient) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        Claims claims = getUserInfo2IdToken(idToken, socialProperties, oauthClient).getBody();
        return userRepository.findByEmail(claims.get(socialProperties.getEmail()).toString())
                .orElseGet(() -> createUser(claims, socialProperties));
    }

//    public User findOrCreateGoogleUser(String idToken, SocialProperties socialProperties, GoogleOauthClient googleOauthClient) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
//        Claims claims = jwtOIDCProvider.getGoogleSignedTokenClaims(idToken, socialProperties.getIss(), socialProperties.getAud(), googleOauthClient).getBody();
//        return userRepository.findByEmail(claims.get(socialProperties.getEmail()).toString())
//                .orElseGet(() -> User.builder()
//                        .email((String) claims.get(socialProperties.getEmail()))
//                        .name((String) claims.get(socialProperties.getEmail()) + UUID.randomUUID())
//                        .socialType(socialProperties.getSocialType())
//                        .build());
//    }

    private User createUser(Claims claims, SocialProperties socialProperties) {
        return User.builder()
                .email((String) claims.get(socialProperties.getEmail()))
                .name((String) claims.get(socialProperties.getName()))
                .profileUrl((String) claims.get(socialProperties.getPicture()))
                .socialType(socialProperties.getSocialType())
                .build();
    }
}
