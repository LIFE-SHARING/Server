package com.umc.lifesharing.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewCreateResultDTO{
        Long reviewId;
        LocalDate createdAt;
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
        Integer reviewCount;
        List<ReviewListDTO> reviewListDTOList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class removeResult{
        Long reviewId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateReview{
        Long reviewId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class deleteReview{
        private Long reviewId;
        private LocalDateTime deletedAt;
    }
}
