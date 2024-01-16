package com.umc.lifesharing.review.dto;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class ReviewRequestDTO {

    @Getter
    public static class ReviewCreateDTO{
        @NotNull
        String content;
        @NotNull
        Integer score;
        @NotNull
        List<String> imageUrl;
    }
}
