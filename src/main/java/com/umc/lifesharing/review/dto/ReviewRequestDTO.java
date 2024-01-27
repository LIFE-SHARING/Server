package com.umc.lifesharing.review.dto;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewRequestDTO {

    @Getter
    @Setter
    public static class ReviewCreateDTO{
        @NotNull
        String content;
        @NotNull
        Integer score;
    }

    @Getter
    @Setter
    public static class ImageUpload{
        List<MultipartFile> image;
    }
}
