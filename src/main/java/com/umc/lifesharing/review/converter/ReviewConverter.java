package com.umc.lifesharing.review.converter;

import com.umc.lifesharing.heart.dto.HeartResponseDTO;
import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.reservation.entity.Reservation;
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
//                .lentDay()  //예약에서 가져오도록 해야함
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
        List<String> imageList = review.getImages().stream()
                .map(ReviewImage::getFullImageUrl)
                .collect(Collectors.toList());

//        List<String> imageList = review.getImages().stream()
//                .map(image -> {
//                    String imageUrl = image.getImageUrl(); // 이미지의 상대 경로
//
//                    // 이미지 URL이 상대 경로인 경우에만 baseUrl을 추가
//                    return imageUrl != null && !imageUrl.startsWith("https") ? "https://lifesharing.s3.ap-northeast-2.amazonaws.com/" + imageUrl : imageUrl;
//                })
//                .collect(Collectors.toList());

//        // 예약 정보가 없을 경우를 고려하여 미리 초기화
//        String reservationTotalTime = "";
//
//        // 예약 정보가 있는 경우에 대해서만 totalTime 설정
//        if (review.getProduct() != null && !review.getProduct().getReservationList().isEmpty()) {
//            Reservation reservation = review.getProduct().getReservationList().get(0); // 여기서 첫 번째 예약 정보를 가져옴
//            reservationTotalTime = reservation.getTotal_time(); // 예약 정보에서 대여기간을 가져와서 설정
//        }

        // 예약 정보가 없을 경우를 고려하여 미리 초기화
        String totalTime = "";

        // 리뷰에 연결된 예약 정보가 있는 경우에만 totalTime 설정
        Reservation reservation = review.getReservation();
        if (reservation != null) {
            totalTime = reservation.getTotalTime();
        }

        return ReviewResponseDTO.ReviewListDTO.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .nickName(review.getUser().getName())
                .imageList(imageList)
                .score(review.getScore())
                .content(review.getContent())
                .lentDay(totalTime)  // 변경된 부분
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ReviewResponseDTO.UserReviewListDTO toUserReviewList(List<Review> reviewList){
        List<ReviewResponseDTO.ReviewListDTO> userReviewList = reviewList.stream()
                .map(ReviewConverter::toReviewList)
                .collect(Collectors.toList());

        return ReviewResponseDTO.UserReviewListDTO.builder()
                .reviewListDTOList(userReviewList)
                .reviewCount(reviewList.size())
                .build();
    }

    public static ReviewResponseDTO.updateReview toUpdateReview(Review review){
        return ReviewResponseDTO.updateReview.builder()
                .reviewId(review.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
