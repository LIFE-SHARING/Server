package com.umc.lifesharing.product.dto;

import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.validation.annotation.ExistCategories;
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
        Long memberId;
        @ExistCategories
        Long categoryId;
        @NotNull @Size(max = 200)
        String name;
        @NotNull @Size(max = 200)
        String content;
        @NotNull
        Integer dayPrice;
        @NotNull
        Integer hourPrice;
        @NotNull
        Integer deposit;
        @NotNull
        String lendingPeriod;
        @NotNull
        List<String> imageUrl;

    }
}
