package com.umc.lifesharing.product.repository;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findByImageUrl(String imageUrl);

    List<ProductImage> findByProductId(Long productId);

    void deleteAllByProduct(Product product);

    void deleteByProductId(Long productId);
}
