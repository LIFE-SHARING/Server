package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.location.dto.LocationDTO;
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

    public static UserResponseDTO.UserInfoResponseDTO toUserInfoResponseDTO(User user) {
        return UserResponseDTO.UserInfoResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getName())
                .locationDTO(new LocationDTO(user.getLocationList().get(0)))
                .build();
    }

    public static UserResponseDTO.ResponseDTO toResponseDTO(User user, String token) {
        return UserResponseDTO.ResponseDTO.builder()
                .userId(user.getId())
                .token(token)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDTO.MyPageResponseDTO toMyPageResponseDTO(User user) {
        return UserResponseDTO.MyPageResponseDTO.builder()
                .userId(user.getId())
                .point(user.getPoint())
                .area(user.getLocationList().get(0).getArea())
                .score(4)      // TODO: 내 product에 대한 score의 평균을 구해서 넣어야함
                .nickname(user.getName())
                .build();
    }
}
