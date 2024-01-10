package com.umc.lifesharing.product.dto;

import com.umc.lifesharing.product.entity.ProductCategory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class ProductRequestDTO {
    @Getter
    public static class RegisterProductDTO{
        @NotNull
        Long member_id;
        @NotNull
        Long category_id;  // 1/10일 - 카테고리 id를 리스트로 돌려받아야함
        @NotNull @Size(max = 200)
        String name;
        @NotNull @Size(max = 200)
        String content;
        @NotNull
        Integer day_price;
        @NotNull
        Integer hour_price;
        @NotNull
        Integer deposit;
        @NotNull
        String least_lent;
        @NotNull
        String lending_period;
        @NotNull
        List<String> image_url;

    }
}
