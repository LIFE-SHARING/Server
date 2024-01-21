package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.location.dto.LocationDTO;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.entity.enum_class.SocialType;
import org.antlr.v4.runtime.Token;
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
                .socialType(SocialType.LIFESHARING)
                .build();
    }

    public static UserResponseDTO.UserInfoResponseDTO toUserInfoResponseDTO(User user) {
        return UserResponseDTO.UserInfoResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getName())
//                .locationDTO(new LocationDTO(user.getLocationList().get(0)))
                .locationDTO(new LocationDTO("위치 관련 기능 미구현", "위치 관련 기능 미구현", "위치 관련 기능 미구현", "위치 관련 기능 미구현","위치 관련 기능 미구현","위치 관련 기능 미구현"))
                .build();
    }

    public static UserResponseDTO.ResponseDTO toResponseDTO(User user, TokenDTO tokenDTO) {
        return UserResponseDTO.ResponseDTO.builder()
                .userId(user.getId())
                .tokenDTO(tokenDTO)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDTO.MyPageResponseDTO toMyPageResponseDTO(User user) {
        return UserResponseDTO.MyPageResponseDTO.builder()
                .userId(user.getId())
                .point(user.getPoint())
//                .area(user.getLocationList().get(0).getArea())
                .area("위치 관련 기능 미구현")
                .score(4)      // TODO: 내 product에 대한 score의 평균을 구해서 넣어야함
                .nickname(user.getName())
                .build();
    }
}
