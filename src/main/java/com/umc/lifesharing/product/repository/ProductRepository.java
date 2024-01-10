package com.umc.lifesharing.product.repository;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);

}
