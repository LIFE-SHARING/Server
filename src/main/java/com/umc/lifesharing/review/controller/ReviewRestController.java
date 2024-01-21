package com.umc.lifesharing.review.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.dto.ReviewResponseDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.service.ReviewCommandService;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import com.umc.lifesharing.validation.annotation.ExistProducts;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/reviews")
public class ReviewRestController {

    private final ReviewCommandService reviewCommandService;

    // 리뷰 등록 API
    @PostMapping("/{userId}")
    @Operation(summary = "리뷰 작성 API")
    public ApiResponse<ReviewResponseDTO.ReviewCreateResultDTO> createReview(
            @ExistMembers @PathVariable(name = "userId") Long userId,
            @ExistProducts @RequestParam(name = "productId") Long productId,
            @Valid @RequestBody ReviewRequestDTO.ReviewCreateDTO request){

        Review review = reviewCommandService.reviewWrite(userId, productId, request);

        return ApiResponse.onSuccess(ReviewConverter.toReviewResultDTO(review));
    }
}
