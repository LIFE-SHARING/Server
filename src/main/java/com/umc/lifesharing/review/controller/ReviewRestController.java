package com.umc.lifesharing.review.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.dto.ReviewResponseDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.service.ReviewCommandService;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import com.umc.lifesharing.validation.annotation.ExistProducts;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/reviews")
public class ReviewRestController {

    private final ReviewCommandService reviewCommandService;

    // 리뷰 등록 API
    @PostMapping("/write/{productId}")
    @Operation(summary = "리뷰 작성 API")
    public ApiResponse<ReviewResponseDTO.ReviewCreateResultDTO> createReview(
            @ExistProducts @PathVariable(name = "productId") Long productId,
            @AuthenticationPrincipal UserAdapter userAdapter,
            @Valid @RequestBody ReviewRequestDTO.ReviewCreateDTO request){

        User loggedInUser = userAdapter.getUser();

        Review review = reviewCommandService.reviewWrite(loggedInUser.getId(), productId, request);

        return ApiResponse.onSuccess(ReviewConverter.toReviewResultDTO(review));
    }
}
