package com.umc.lifesharing.review.rerpository;

import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    Optional<ReviewImage> findByImageUrl(String imageUrl);
}
