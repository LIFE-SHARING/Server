package com.umc.lifesharing.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserResponseDTO {

    // 회원이 등록한 제품을 조회할 것임
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPreviewDTO{
        String name;
        String location;
        Float score;
        Integer score_count;
        Integer deposit;
        Integer day_price;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductPreviewListDTO{
        List<ProductPreviewDTO> productList;
    }
}
