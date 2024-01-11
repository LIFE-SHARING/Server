package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserConverter {
//    private PasswordEncoder passwordEncoder;

    public static User toUser(UserRequestDTO.JoinDTO joinDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(joinDTO.getEmail())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .phone(joinDTO.getPhone())
                .name(joinDTO.getName())
                .build();
    }

    public static UserResponseDTO.ResponseDTO toResponseDTO(User user, String token) {
        return UserResponseDTO.ResponseDTO.builder()
                .id(user.getId())
                .token(token)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
