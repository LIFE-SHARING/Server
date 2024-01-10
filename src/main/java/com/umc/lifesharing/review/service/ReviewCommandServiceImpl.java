package com.umc.lifesharing.review.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.review.converter.ReviewConverter;
import com.umc.lifesharing.review.dto.ReviewRequestDTO;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService{

    private final ReviewRepository reviewRepository;
    private final ProductQueryService productQueryService;
    private final UserQueryService userQueryService;
    @Override
    public Review reviewWrite(Long userId, Long productId, ReviewRequestDTO.ReviewCreateDTO request) {
        Review newReview = ReviewConverter.toReview(request);

        newReview.setProduct(productQueryService.findByProduct(productId).get());
        newReview.setUser(userQueryService.findByUser(userId).get());
        
        return reviewRepository.save(newReview);
    }
}
