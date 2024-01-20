package com.umc.lifesharing.heart.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;

import java.util.List;

public interface HeartCommandService {

    boolean addHeart(User user, Product product);
    boolean removeHeart(User user, Product product);

    List<Product> getFavoriteProducts(Long userId);
}
