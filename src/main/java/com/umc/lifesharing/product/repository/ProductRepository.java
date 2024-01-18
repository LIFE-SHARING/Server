package com.umc.lifesharing.product.repository;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    List<Product> findByCategoryId(Long categoryId);

    // 제품의 평점과 리뷰 개수 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.score = ROUND((p.score * p.reviewCount + :newScore) / (p.reviewCount + 1), 2), p.reviewCount = p.reviewCount + 1 WHERE p.id = :productId")
    void updateScore(@Param("productId") Long productId, @Param("newScore") Integer newScore);

    // 최신순 필터
    List<Product> findAllByOrderByCreatedAtDesc();

    // 인기순 필터
    List<Product> findAllByOrderByScoreDesc();

    // 리뷰순 필터
    List<Product> findAllByOrderByReviewCountDesc();

    List<Product> findAllByUser(User user);

}
