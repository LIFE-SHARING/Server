package com.umc.lifesharing.review.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandServiceImpl;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService{

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductCommandServiceImpl productCommandService;

    @Override
    public Review reviewWrite(Long userId, Long productId, ReviewRequestDTO.ReviewCreateDTO request, List<String> uploadedFileNames) {
        Review newReview = ReviewConverter.toReview(request);

        Product product = productRepository.findById(productId).get();
        User user = userRepository.findById(userId).get();

        newReview.setProduct(product);
        newReview.setUser(user);
        newReview.setImageUrl(uploadedFileNames);

        // 해당 제품의 score 업데이트
        productCommandService.updateProductScore(productId, request.getScore());
        
        return reviewRepository.save(newReview);
    }
}
