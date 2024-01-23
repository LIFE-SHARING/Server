package com.umc.lifesharing.product.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;

import java.util.List;

public interface ProductCommandService {

    // 제품 등록
    Product ProductRegister(ProductRequestDTO.RegisterProductDTO request, UserAdapter userAdapter, List<String> uploadedFileNames);

    // 제품 삭제
    void deleteProduct(Long productId, Long userId);

    // 제품 상세 조회
    ProductResponseDTO.ProductDetailDTO productDetail(Long productId, UserAdapter userAdapter);

    // 제품에 대한 좋아요 판별(회원 기준)
    boolean isProductLikedByUser(Product product, UserAdapter userAdapter);

    // 카테고리별 제품 조회
    List<Product> getProductsByCategory(Long categoryId);

    // 별점 평균 업데이트
    void updateProductScore(Long productId, Integer newScore);

    // 제품 정보 수정
    Product updateProduct(Long productId, ProductRequestDTO.UpdateProductDTO request, UserAdapter userAdapter);

    // 홈에서 필터별 제품 조회
    List<Product> getHomeProduct(String filter);

    // 제품 검색 필터별 조회
    List<Product> getSearchProduct(String filter, String keyword);
}
