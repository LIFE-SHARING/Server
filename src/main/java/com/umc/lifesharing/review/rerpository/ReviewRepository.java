package com.umc.lifesharing.review.rerpository;

import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

    List<Review> findByProductId(Long productId);

    @Query("SELECT r FROM Review r JOIN FETCH r.product WHERE r.user.id = :userId")
    List<Review> findByUserIdWithProduct(@Param("userId") Long userId);

    List<Review> findAllByUserId(Long userId);
}
