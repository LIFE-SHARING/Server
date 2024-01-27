package com.umc.lifesharing.review.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandServiceImpl;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService{

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductCommandServiceImpl productCommandService;

    @Override
    public Review reviewWrite(UserAdapter userAdapter, Long productId, ReviewRequestDTO.ReviewCreateDTO request, List<String> uploadedFileNames) {
        Review newReview = ReviewConverter.toReview(request);

        User loggendIdUser = userAdapter.getUser();

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        User user = userRepository.findById(loggendIdUser.getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        newReview.setProduct(product);
        newReview.setUser(user);

        // 이미지 URL을 ReviewImage 엔티티로 매핑하여 리스트에 추가
        for (String imageUrl : uploadedFileNames) {
            ReviewImage reviewImage = ReviewImage.create(imageUrl);
            reviewImage.setReview(newReview);
            newReview.getImages().add(reviewImage);
        }
        // 해당 제품의 score 업데이트
        productCommandService.updateProductScore(productId, request.getScore());
        
        return reviewRepository.save(newReview);
    }

    @Override
    public List<Review> getUserReview(UserAdapter userAdapter) {
        User loggendInUser = userAdapter.getUser();

        List<Review> userReviewList = reviewRepository.findByUserId(loggendInUser.getId());

        return userReviewList;
    }
}
