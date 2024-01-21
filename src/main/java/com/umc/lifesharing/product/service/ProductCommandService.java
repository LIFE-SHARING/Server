package com.umc.lifesharing.product.service;

import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;

import java.util.List;

public interface ProductCommandService {

    // 제품 등록
    Product ProductRegister(ProductRequestDTO.RegisterProductDTO request);

    // 제품 삭제
    void deleteProduct(Long productId, Long userId);

    // 제품 상세 조회
    ProductResponseDTO.ProductDetailDTO productDetail(Long productId);

    // 카테고리별 제품 조회
    List<Product> getProductsByCategory(Long categoryId);

    // 별점 평균 업데이트
    void updateProductScore(Long productId, Integer newScore);
}
