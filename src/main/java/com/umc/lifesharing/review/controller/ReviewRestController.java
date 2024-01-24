package com.umc.lifesharing.review.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.converter.HeartConverter;
import com.umc.lifesharing.heart.dto.HeartResponseDTO;
import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.dto.ReviewResponseDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.review.service.ReviewCommandService;
import com.umc.lifesharing.review.service.ReviewQueryService;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import com.umc.lifesharing.validation.annotation.ExistProducts;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/reviews")
public class ReviewRestController {

    private final ReviewCommandService reviewCommandService;
    private final ReviewRepository reviewRepository;
    private final AwsS3Service awsS3Service;

    // 리뷰 등록 API
//    @PostMapping("/write/{productId}")
//    @Operation(summary = "리뷰 작성 API")
//    public ApiResponse<ReviewResponseDTO.ReviewCreateResultDTO> createReview(@ExistProducts @PathVariable(name = "productId") Long productId, @AuthenticationPrincipal UserAdapter userAdapter,
//                                                                             @Valid @RequestPart(name = "request") ReviewRequestDTO.ReviewCreateDTO request, @RequestPart(name = "files") List<MultipartFile> files) {
//        // 파일 업로드 처리
//        List<String> uploadedFileNames = awsS3Service.uploadReviewFiles(files);
//        // 리뷰 작성 및 파일명 등록
//        Review review = reviewCommandService.reviewWrite(userAdapter, productId, request, uploadedFileNames);
//
//        return ApiResponse.onSuccess(ReviewConverter.toReviewResultDTO(review));
//    }
    @PostMapping("/write/{reservationId}")
    @Operation(summary = "리뷰 작성 API")
    public ApiResponse<ReviewResponseDTO.ReviewCreateResultDTO> createReview(@PathVariable(name = "reservationId") Long reservationId, @AuthenticationPrincipal UserAdapter userAdapter,
                                                                             @Valid @RequestPart(name = "request") ReviewRequestDTO.ReviewCreateDTO request, @RequestPart(name = "files") List<MultipartFile> files) {
        // 파일 업로드 처리
        List<String> uploadedFileNames = awsS3Service.uploadReviewFiles(files);
        // 리뷰 작성 및 파일명 등록
        Review review = reviewCommandService.reviewWrite(userAdapter, reservationId, request, uploadedFileNames);

        return ApiResponse.onSuccess(ReviewConverter.toReviewResultDTO(review));
    }

    // 리뷰 리스트
    @GetMapping("/list")
    @Operation(summary = "사용자 리뷰 목록 조회 API")
    public ApiResponse<ReviewResponseDTO.UserReviewListDTO> getReviewList(@AuthenticationPrincipal UserAdapter userAdapter){
        List<Review> reviewList = reviewCommandService.getUserReview(userAdapter);
        return ApiResponse.onSuccess(ReviewConverter.toUserReviewList(reviewList));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "리뷰 삭제 API")
    public ApiResponse<String> remove(@RequestParam Long reviewId, @AuthenticationPrincipal UserAdapter userAdapter){

        Long loggedInUser = userAdapter.getUser().getId();

        try {
            reviewCommandService.deleteReview(reviewId, loggedInUser);
            return ApiResponse.onSuccess("성공적으로 삭제되었습니다.");
        } catch (NotFoundException e) {
            return ApiResponse.onFailure(ErrorStatus.ALREADY_DELETE_REVIEW.getCode(), ErrorStatus.ALREADY_DELETE_REVIEW.getMessage(), null);
        } catch (Exception e) {
            return ApiResponse.onFailure(ErrorStatus._INTERNAL_SERVER_ERROR.getCode(), ErrorStatus._INTERNAL_SERVER_ERROR.getMessage(), null);
        }
    }
}


// 사용자 리뷰 목록을 가져오는데 리뷰 테이블에서 어떤 회원의 아이디를 넘겨 주면 그 회원에 해당하는 리뷰 목록들을 가져 올 것이다.
// 그러면 그 리뷰리스트의 각 리뷰에서 제품 아이디와 그 사용자에 해당하는 연결된 예약정보에서 대여기간을 가져오도록 해야함