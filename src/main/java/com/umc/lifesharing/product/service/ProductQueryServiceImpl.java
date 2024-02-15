package com.umc.lifesharing.product.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.repository.ProductCategoryRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<String> findNameByIdIn(List<Long> id) {
        return productCategoryRepository.findNameByIdIn(id);
    }

}
