package com.umc.lifesharing.product.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;

import java.util.Optional;

public interface ProductQueryService {

    Optional<ProductCategory> findByCategory(Long id);
    Optional<Product> findByProduct(Long id);
}
