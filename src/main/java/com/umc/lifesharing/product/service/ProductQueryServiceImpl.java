package com.umc.lifesharing.product.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.repository.ProductCategoryRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService{

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public Optional<ProductCategory> findByCategory(Long id) {
        return productCategoryRepository.findById(id);
    }
    @Override
    public Optional<Product> findByProduct(Long id) {
        return productRepository.findById(id);
    }
}
