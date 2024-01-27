package com.umc.lifesharing.review.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;

import java.util.List;

public interface ReviewCommandService {
    Review reviewWrite(UserAdapter userAdapter, Long productId, ReviewRequestDTO.ReviewCreateDTO request, List<String> uploadedFileNames);

    List<Review> getUserReview(UserAdapter userAdapter);
}
