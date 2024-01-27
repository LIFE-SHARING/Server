package com.umc.lifesharing.product.repository;

import com.umc.lifesharing.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
