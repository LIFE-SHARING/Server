package com.umc.lifesharing.product.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ProductQueryService {

    Optional<ProductCategory> findByCategory(Long id);

    Optional<Product> findByProduct(Long id);
}
