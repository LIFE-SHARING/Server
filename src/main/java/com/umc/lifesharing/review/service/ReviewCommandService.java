package com.umc.lifesharing.review.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewCommandService {
    Review reviewWrite(UserAdapter userAdapter, Long reservationId, ReviewRequestDTO.ReviewCreateDTO request, List<String> uploadedFileNames);

    List<Review> getUserReview(UserAdapter userAdapter);

    void deleteReview(Long reviewId, Long userId);

    Review updateReview(Long reviewId, UserAdapter userAdapter, ReviewRequestDTO.reviewUpdateDTO request);

    void updateReviewImage(Long reviewId, UserAdapter userAdapter, List<MultipartFile> imageList);

    Integer otherUserReviewCount(Long userId);

}
