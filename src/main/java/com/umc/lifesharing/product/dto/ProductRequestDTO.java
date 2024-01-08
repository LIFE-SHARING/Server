package com.umc.lifesharing.product.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class ProductRequestDTO {
    @Getter
    public static class RegisterProductDTO{  // 제품 등록
        @NotBlank
        String name;
        @NotNull
        Long category_id;
        @NotNull
        Integer day_price;
        @NotNull
        Integer hour_price;
        @NotNull
        Integer deposit;
        @NotNull
        Integer least_lent;
        @NotBlank
        String lending_period;
        @NotBlank
        @Size(min = 0, max = 200)
        String content;
        @NotBlank
        String image_url;
    }
}
