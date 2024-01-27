package com.umc.lifesharing.product.repository;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
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
    @Transactional(readOnly = true)
    List<Product> findAllByOrderByCreatedAtDesc();

    // 인기순 필터
    List<Product> findAllByOrderByScoreDesc();

    // 리뷰순 필터
    List<Product> findAllByOrderByReviewCountDesc();

    // 제품이름 + 최신순 필터
    List<Product> findByNameContainingOrderByCreatedAtDesc(String keyword);

    // 제품이름 + 인기순 필터
    List<Product> findByNameContainingOrderByScoreDesc(String keyword);

    // 제품이름 + 리뷰순 필터
    List<Product> findByNameContainingOrderByReviewCountDesc(String keyword);

    // 회원 + 최신순
    List<Product> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 회원 + 인기순
    List<Product> findByUserIdOrderByScoreDesc(Long userId);

    // 회원 + 리뷰순
    List<Product> findByUserIdOrderByReviewCountDesc(Long userId);

    List<Product> findAllByUser(User user);

    // 제품 - 사용자(닉네임, 프로필이미지)
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.user WHERE p.id = :productId")
    Optional<Product> findProductWithUser(@Param("productId") Long productId);

    // 제품 - 리뷰
//    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.reviewList WHERE p.id = :productId")
//    Optional<Product> findProductWithReviews(@Param("productId") Long productId);

}
