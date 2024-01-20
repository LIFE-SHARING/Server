package com.umc.lifesharing.heart.repository;

import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByUserAndProduct(User user, Product product);

    List<Heart> findByUserId(Long userId);

}
