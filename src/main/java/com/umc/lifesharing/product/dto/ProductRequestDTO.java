package com.umc.lifesharing.product.dto;

import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.validation.annotation.ExistCategories;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class ProductRequestDTO {
    @Getter
    @Setter
    public static class RegisterProductDTO{
        @NotNull
        String categoryIds; // 카테고리 다중 선택을 위해 Long -> String 으로 수정
        @NotNull @Size(max = 200)
        String name;
        @NotNull @Size(max = 200)
        String content;
        @NotNull(message = "일일 대여비를 입력하세요.")
        Integer dayPrice;
        @NotNull(message = "시간 당 대여비를 입력하세요.")
        Integer hourPrice;
        @NotNull(message = "보증금을 입력하세요.")
        Integer deposit;
        @NotNull
        String lendingPeriod;
    }

    @Getter
    @Setter
    public static class UpdateProductDTO{
        //@ExistCategories
        String categoryIds; // 카테고리 다중 선택을 위해 Long -> String 으로 수정
        String name;
        String content;
        Integer dayPrice;
        Integer hourPrice;
        Integer deposit;
        String lendingPeriod;
    }
}
