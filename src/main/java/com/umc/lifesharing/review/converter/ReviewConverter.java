package com.umc.lifesharing.review.converter;

import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.dto.ReviewResponseDTO;
import com.umc.lifesharing.review.entity.Review;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReviewConverter {

    // 회원이 제품에 대한 리뷰 생성 요청 값을 빌더 패턴으로 Review엔티티에 저장 (?)
    public static Review toReview(ReviewRequestDTO.ReviewCreateDTO request){
        return Review.builder()
//                .product_name(request.getProduct_name())
                .content(request.getContent())
                .score(request.getScore())
                .image_url(new ArrayList<>())
                .build();
    }

    public static ReviewResponseDTO.ReviewCreateResultDTO toReviewResultDTO(Review review){
        return ReviewResponseDTO.ReviewCreateResultDTO.builder()
                .reviewId(review.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
