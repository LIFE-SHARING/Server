package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.dto.UserResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    // 회원별 제품 조회 응답
    public static UserResponseDTO.ProductPreviewDTO productPreviewDTO(Product product){
        return UserResponseDTO.ProductPreviewDTO.builder()
                .name(product.getName())
                .deposit(product.getDeposit())
                .day_price(product.getDay_price())
                .score(product.getScore()) //별점(평균으로 가져오도록 해야함 - 구현전)
                .score_count(product.getScore_count()) //리뷰 개수(해당 제품에 대한 리뷰 개수를 카운트해야함 - 구현전)
                //위치
                .build();
    }

    public static UserResponseDTO.ProductPreviewListDTO productPreviewListDTO(List<Product> productList){
        List<UserResponseDTO.ProductPreviewDTO> productPreViewDTOList = productList.stream()
                .map(UserConverter::productPreviewDTO).collect(Collectors.toList());

        return UserResponseDTO.ProductPreviewListDTO.builder()
                .productList(productPreViewDTOList)
                .build();
    }
}
