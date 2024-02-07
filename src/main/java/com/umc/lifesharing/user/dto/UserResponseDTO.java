package com.umc.lifesharing.user.dto;

import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.location.dto.LocationDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;


public class UserResponseDTO {

    // 회원이 등록한 제품 조회
    @Builder
    @Getter
    @Setter
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
        String isReserved;   // 대여 여부(대여중 false / 대여가능 true)
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPreviewListDTO{
//        Long userId;
//        String userName;
//        String imageUrl;
//        String location;  // 대여자 위치 정보
//        Integer score;
//        Integer reviewCount;
//        Integer productCount;
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
        private String area;    // 읍/면/동    ex. 무거동
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
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListDTO{
        Long reviewId;
        Long userId;
        String nickName;
        String profileUrl;
        LocalDate createdAt;
        String lentDay;  // 이후에 Reservation lent_day로 가져와야 함
        List<String> imageList;
        Integer score;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserReviewListDTO{
//        Long userId;
//        String userName;
//        String imageUrl;
//        String location;  // 대여자 위치 정보
//        Integer score;
//        Integer reviewCount;
        List<ReviewListDTO> reviewList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileDTO {
        Long userId;
        String userName;
        String imageUrl;
        String location;  // 대여자 위치 정보
        Integer score;
        Integer reviewCount;
        Integer productCount;
        Integer rentProductCnt;
    }
}
