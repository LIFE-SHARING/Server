package com.umc.lifesharing.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.umc.lifesharing.user.dto.UserResponseDTO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface SocialService {
    public UserResponseDTO.ResponseDTO socialJoin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException;
    public UserResponseDTO.ResponseDTO socialLogin(String idToken) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException;

}
