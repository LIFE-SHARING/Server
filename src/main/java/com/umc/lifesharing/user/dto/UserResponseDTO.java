package com.umc.lifesharing.user.dto;

import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.location.dto.LocationDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;


public class UserResponseDTO {

    // 회원이 등록한 제품 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPreviewDTO{
        Long productId;
        String name;
        String location;
        Integer score;
        Integer reviewCount;
        Integer deposit;
        Integer dayPrice;
        String imageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPreviewListDTO{
        List<ProductPreviewDTO> productList;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResponseDTO {   // 로그인 회원가입 용 응답 dto
        private Long userId;
        private LocalDateTime createdAt;
        private TokenDTO tokenDTO;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserInfoResponseDTO {
        private Long userId;
        private String email;
        private String nickname;
        private String phone;
        private String profileUrl;
        private LocationDTO.ResponseDTO locationDTO;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MyPageResponseDTO {
        private Long userId;    // pk
        private String nickname;    // 닉네임
        private String dong;    // 읍/면/동    ex. 무거동
        private Integer score;
        private String profileUrl;
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

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CheckNicknameResponseDTO {
        private String message;
        private Boolean existNickname;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateInquiryDTO {
        private Long inquiryId;
        private LocalDateTime createdAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InquiryDTO {
        private Long inquiryId;
        private String title;
        private String body;
        private List<String> imageUrlList;
        private LocalDateTime createdAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InquiryPreviewDTO {
        private List<InquiryDTO> inquiryList;
    }
}
