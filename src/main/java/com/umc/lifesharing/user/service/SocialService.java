package com.umc.lifesharing.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import io.jsonwebtoken.Claims;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Deprecated
public interface SocialService {
    UserResponseDTO.ResponseDTO socialLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException;
}
