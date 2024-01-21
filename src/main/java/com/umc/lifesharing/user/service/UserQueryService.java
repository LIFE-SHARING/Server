package com.umc.lifesharing.user.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;
import java.util.List;
import java.util.Optional;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserQueryService extends UserDetailsService {
    UserResponseDTO.MyPageResponseDTO getMyPage(UserAdapter userAdapter);
    UserResponseDTO.UserInfoResponseDTO getUserInfo(UserAdapter userAdapter);
    Optional<User> findByUser(Long id);

    // 회원의 제품 리스트
    List<Product> getProductList(Long memberId);
}
