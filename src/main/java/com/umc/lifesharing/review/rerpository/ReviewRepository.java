package com.umc.lifesharing.review.rerpository;

import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

}
