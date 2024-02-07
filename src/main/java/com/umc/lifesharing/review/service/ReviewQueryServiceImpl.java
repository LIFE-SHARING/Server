package com.umc.lifesharing.review.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewQueryServiceImpl implements ReviewQueryService{

//    private final ReviewRepository reviewRepository;
//    @Override
//    public boolean reviewDelete(Long reviewId, UserAdapter userAdapter) {
//        Long userId = userAdapter.getUser().getId();
//        Review review = reviewRepository.findByUserIdAndUserId(reviewId, userId);
//
//        if (review != null){
//            reviewRepository.delete(review);
//            return true;   // 삭제 성공 시
//        }
//        return false;  // 삭제 실패시
//    }
}
