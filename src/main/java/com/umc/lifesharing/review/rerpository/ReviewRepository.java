package com.umc.lifesharing.review.rerpository;

import com.umc.lifesharing.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
