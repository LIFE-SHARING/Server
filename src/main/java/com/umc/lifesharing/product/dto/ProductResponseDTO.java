package com.umc.lifesharing.product.dto;

import com.umc.lifesharing.review.entity.Review;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProductResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterResultDTO{   // 제품 등록 응답
        Long productId;
        LocalDateTime createdAt;
    }

    // 홈 제품 조회, 카테고리별 제품 조회
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeResultDTO{
        Long productId;
        String name;
        String location;  //사용자로부터 위치 정보를 가져와야함
        Integer deposit;
        Integer dayPrice;
        Integer score;
        Integer reviewCount;
        String image_url;  //이미지 처리 방법 알아내기
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomePreviewListDTO{
        List<ProductResponseDTO.HomeResultDTO> productResultDTOList;
    }

    // 제품 상세 조회
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetailDTO{
        Long productId;
        Long categoryId;
        Long userId;
        String categoryName;
        String location;  // 위치정보
        List<String> imageUrl;
        String name;
        Integer score;
        Integer reviewCount;
        Integer deposit;
        Integer dayPrice;
        Integer hourPrice;
        Boolean isLiked; //찜여부
        String content;
        List<ReviewListDTO> reviewList;   //리뷰 리스트를 출력해야함
    }

    // 제품 상세 조회 시 리뷰 리스트에 필요한 정보
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListDTO{
        Long reviewId;
        Long userId;
        LocalDate createdAt;
        Integer lentDay;  // 이후에 Reservation lent_day로 가져와야 함
        List<String> imageList;
        Integer score;
        String content;
    }

    // 제품 검색 시 필요 정보
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchListDTO{
        Long product_id;
        String name;
        String location;  //사용자로부터 위치 정보를 가져와야함
        Integer deposit;
        Integer day_price;
        Integer hour_price;
        Integer score;
        Integer review_count;
        Boolean isLiked;
        String image_url;  //이미지 처리 방법 알아내기
    }

    // 제품 정보 수정
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResDTO{
        Long productId;
        LocalDateTime updatedAt;
    }

}
