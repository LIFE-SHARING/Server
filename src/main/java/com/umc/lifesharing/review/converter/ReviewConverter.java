package com.umc.lifesharing.review.converter;

import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.dto.ReviewResponseDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewConverter {

    // 회원이 제품에 대한 리뷰 생성 요청 값을 빌더 패턴으로 Review엔티티에 저장 (?)
    public static Review toReview(ReviewRequestDTO.ReviewCreateDTO request){
        return Review.builder()
                .content(request.getContent())
                .score(request.getScore())
//                .lent_day()  예약에서 가져오도록 해야함
                .images(new ArrayList<>())
                .build();
    }

    public static ReviewResponseDTO.ReviewCreateResultDTO toReviewResultDTO(Review review){
        return ReviewResponseDTO.ReviewCreateResultDTO.builder()
                .reviewId(review.getId())
                .createdAt(review.getCreatedAt())
                .build();
    }

    // 사용자가 등록한 리뷰 목록 조회
    public static ReviewResponseDTO.ReviewListDTO toReviewList(Review review){
        List<String> imageList = review.getImages().stream().map(ReviewImage::getImageUrl).collect(Collectors.toList());

        return ReviewResponseDTO.ReviewListDTO.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .nickName(review.getUser().getName())
                .imageList(imageList)
                .score(review.getScore())
                .content(review.getContent())
                .lentDay(review.getLentDay())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ReviewResponseDTO.UserReviewListDTO toUserReviewList(List<Review> reviewList){
        List<ReviewResponseDTO.ReviewListDTO> userReviewList = reviewList.stream()
                .map(ReviewConverter::toReviewList)
                .collect(Collectors.toList());

        return ReviewResponseDTO.UserReviewListDTO.builder()
                .reviewListDTOList(userReviewList)
                .build();
    }
}
