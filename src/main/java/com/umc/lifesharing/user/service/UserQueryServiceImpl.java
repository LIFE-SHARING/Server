package com.umc.lifesharing.user.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        Optional<User> member = userRepository.findById(memberId);

        return member.map(User::getProductList).orElse(Collections.emptyList());
    }
}
