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
    public static class ResponseDTO {
        private Long id;
        private String token;
        private LocalDateTime createdAt;
    }



}
