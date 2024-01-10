package com.umc.lifesharing.user.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> findByUser(Long id);

    // 회원의 제품 리스트
    List<Product> getProductList(Long memberId);
}
