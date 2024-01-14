package com.umc.lifesharing.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResponseDTO {   // 로그인 회원가입 용 응답 dto
        private Long id;
        private String token;
        private LocalDateTime createdAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MyPageResponseDTO {
        private Long userId;    // pk
        private String name;    // 닉네임
        private String area;    // 읍/면/동    ex. 무거동
        private Integer score;  // 후기 평균
        private Long point;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ChangePasswordResponseDTO {
        private Boolean isChanged;
        private LocalDateTime updatedAt;
    }

}
