package com.umc.lifesharing.user.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductStatus;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    @Override
    public Optional<User> findByUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<Product> getProductList(Long memberId) {
        Optional<User> mem = userRepository.findById(memberId);

        return mem.map(member -> {
            // 회원이 가진 Product 중 상태가 "EXIST"인 것만 필터링하여 반환
            return member.getProductList().stream()
                    .filter(product -> ProductStatus.EXIST.equals(product.getProduct_status()))
                    .collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }
}
