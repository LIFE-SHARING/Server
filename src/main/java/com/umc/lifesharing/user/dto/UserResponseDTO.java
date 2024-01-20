package com.umc.lifesharing.user.dto;


import com.umc.lifesharing.location.dto.LocationDTO;
import com.umc.lifesharing.location.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;


public class UserResponseDTO {

    // 회원이 등록한 제품을 조회할 것임
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
        private String token;
        private LocalDateTime createdAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserInfoResponseDTO {   // 로그인 회원가입 용 응답 dto
        private Long userId;
        private String email;
        private String nickname;
        private String phone;
        private LocationDTO locationDTO;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MyPageResponseDTO {
        private Long userId;    // pk
        private String nickname;    // 닉네임
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
