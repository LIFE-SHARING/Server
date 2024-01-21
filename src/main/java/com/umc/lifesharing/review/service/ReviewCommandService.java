package com.umc.lifesharing.review.service;

import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;

import java.util.List;

public interface ReviewCommandService {
    Review reviewWrite(Long userId, Long productId, ReviewRequestDTO.ReviewCreateDTO request, List<String> uploadedFileNames);

}
