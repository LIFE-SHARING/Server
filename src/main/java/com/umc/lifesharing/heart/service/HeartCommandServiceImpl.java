package com.umc.lifesharing.heart.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.heart.repository.HeartRepository;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartCommandServiceImpl implements HeartCommandService{

    private final HeartRepository heartRepository;

    @Override
    public boolean addHeart(User user, Product product) {

        Optional<Heart> existHeart = heartRepository.findByUserAndProduct(user, product);

        if (existHeart.isPresent()) {  // 이미 좋아요 한 경우
           return false;  // 이미 좋아요가 있으면 false;
        }
        else {
            Heart heart = new Heart(user, product);
            heartRepository.save(heart);

            return true;   // 좋아요 생성 성공하면 true;
        }
    }

    @Override
    public boolean removeHeart(User user, Product product) {

        Optional<Heart> existHeart = heartRepository.findByUserAndProduct(user, product);

        if(existHeart.isPresent()) {  // 좋아요가 있으면 삭제
            heartRepository.findByUserAndProduct(user, product).ifPresent(heartRepository::delete);
            return true;
        }
        else {  // 이미 좋아요가 삭제된 경우
            return false;
        }
    }


    @Override
    public List<Product> getFavoriteProducts(UserAdapter userAdapter) {

        Long userId = userAdapter.getUser().getId();

        List<Heart> heartList = heartRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return heartList.stream()
                .map(Heart::getProduct)
                .collect(Collectors.toList());
    }
}

