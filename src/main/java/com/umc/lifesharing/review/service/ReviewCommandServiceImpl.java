package com.umc.lifesharing.review.service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.apiPayload.exception.handler.ReservationHandler;
import com.umc.lifesharing.apiPayload.exception.handler.ReviewHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandServiceImpl;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.repository.ReservationRepository;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;
import com.umc.lifesharing.review.rerpository.ReviewImageRepository;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService{

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final ProductCommandServiceImpl productCommandService;

    @Value("${s3.url}")
    private String url;

    @Override
    public Review reviewWrite(UserAdapter userAdapter, Long reservationId, ReviewRequestDTO.ReviewCreateDTO request, List<String> uploadedFileNames) {
        Review newReview = ReviewConverter.toReview(request);

        User loggendIdUser = userAdapter.getUser();

        // 예약 번호를 가지고 리뷰를 작성하도록 함
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationHandler(ErrorStatus.ORDER_ID_NOT_FOUND));
        // 예약에 속한 제품 정보 가져오기
        Product product = reservation.getProduct();
        User user = userRepository.findById(loggendIdUser.getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        newReview.setReservation(reservation);
        newReview.setProduct(product);
        newReview.setUser(user);

        // 이미지 URL을 ReviewImage 엔티티로 매핑하여 리스트에 추가
        for (String imageUrl : uploadedFileNames) {
            ReviewImage reviewImage = ReviewImage.create(imageUrl);
            reviewImage.setReview(newReview);
            newReview.getImages().add(reviewImage);
        }
        // 해당 제품의 score 업데이트
        productCommandService.updateProductScore(product.getId(), request.getScore());
        
        return reviewRepository.save(newReview);
    }

    private static String generateFullImageUrl(String fileName) {
        return "https://lifesharing.s3.ap-northeast-2.amazonaws.com/" + fileName;
    }

    // 사용자 리뷰 목록 조회
    @Override
    public List<Review> getUserReview(UserAdapter userAdapter) {
        User loggendInUser = userAdapter.getUser();

        List<Review> userReviewList = reviewRepository.findByUserIdWithProduct(loggendInUser.getId());

        return userReviewList;
    }

    // 리뷰 삭제
    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId).get();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        if (review.getUser().getId().equals(user.getId())){
            List<ReviewImage> reviewImages = review.getImages();
            for(ReviewImage reviewImage : reviewImages){
                // S3에서 이미지 파일 삭제
                awsS3Service.deleteReviewFile(reviewImage.getImageUrl());
                reviewImageRepository.delete(reviewImage);
            }
            // 리뷰 삭제
            reviewRepository.delete(review);
        }
        else {
            throw new NotFoundException("로그인해주세요.");
        }
    }

    // 리뷰 내용 수정
    @Override
    public Review updateReview(Long reviewId, UserAdapter userAdapter, ReviewRequestDTO.reviewUpdateDTO request) {
        //기존 리뷰 정보 가져오기
        Review existReview = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));

        User loggendInUser = userAdapter.getUser();
        if (!existReview.getUser().getId().equals(loggendInUser.getId())){
            throw new UserHandler(ErrorStatus.USER_NOT_FOUNDED);
        }
        // 수정된 정보 업데이트
        if (request.getContent() != null){ existReview.setContent(request.getContent()); }
        if (request.getScore() != null) {existReview.setScore(request.getScore()); }

        // 리뷰 저장
        return reviewRepository.save(existReview);
    }

    // 리뷰 이미지 수정
    @Override
    public void updateReviewImage(Long reviewId, UserAdapter userAdapter, List<MultipartFile> imageList) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewHandler(ErrorStatus.REVIEW_NOT_FOUND));
        User user = userRepository.findById(userAdapter.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        if (review.getUser().getId().equals(user.getId())) {
            // 기존의 이미지 리스트를 삭제
            review.getImages().forEach(reviewImage -> {
                awsS3Service.deleteReviewFile(reviewImage.getImageUrl());
                reviewImageRepository.delete(reviewImage);
            });
            review.getImages().clear();

            // 새로운 이미지 리스트 추가
            List<String> uploadedFileNames = awsS3Service.uploadReviewFiles(imageList);
            for (String imageUrl : uploadedFileNames) {
                ReviewImage newReviewImage = ReviewImage.create(review, imageUrl, url + imageUrl);
                review.getImages().add(newReviewImage);
            }
        }
    }

    private String getOriginalFileName(String fileName) {
        // 파일의 원본 이름을 얻기 위한 로직 추가
        return fileName.substring(fileName.lastIndexOf("_") + 1);
    }

}
