package com.umc.lifesharing.product.converter;

import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProductConverter {
    public static Product toProduct(ProductRequestDTO.RegisterProductDTO request){

        return Product.builder()
                .name(request.getName())
                .content(request.getContent())
                .day_price(request.getDay_price())
                .hour_price(request.getHour_price())
                .deposit(request.getDeposit())
                .least_lent(request.getLeast_lent())
                .lending_period(request.getLending_period())
                .image_url(new ArrayList<>())  // 1/10일 - 이미지도 리스트 형태로 받아와야함(구현전)
                .build();
    }


    public static ProductResponseDTO.RegisterResultDTO toRegisterResultDTO(Product product){
        return ProductResponseDTO.RegisterResultDTO.builder()
                .productId(product.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
