package com.umc.lifesharing.heart.converter;

import com.umc.lifesharing.heart.dto.HeartResponseDTO;
import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HeartConverter {

    public static HeartResponseDTO.createResult toCreateHeart(Heart heart){
        return HeartResponseDTO.createResult.builder()
//                .productId(heart.getProduct().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static HeartResponseDTO.removeResult toRemoveHeart(Heart heart){
        return HeartResponseDTO.removeResult.builder()
                .productId(heart.getProduct().getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 제품 이미지 가져오도록
    private static List<String> getProductImagUrls(Product product){
        return product.getImages().stream().map(ProductImage::getImageUrl).collect(Collectors.toList());
    }

    // 찜한 목록
    public static HeartResponseDTO.heartResult toHeartList(Product product){
        List<String> imageUrls = getProductImagUrls(product);

        String firstImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        return HeartResponseDTO.heartResult.builder()
                .productId(product.getId())
                .name(product.getName())
                .score(product.getScore())
                .reviewCount(product.getReviewCount())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .location("사용자로부터 받아오기")
                .image_url(firstImageUrl)
                .build();
    }

    public static HeartResponseDTO.HeartPreviewListDTO heartPreviewListDTO(List<Product> productList){
        List<HeartResponseDTO.heartResult> heartResultList = productList.stream()
                .map(HeartConverter::toHeartList)
                .collect(Collectors.toList());

        return HeartResponseDTO.HeartPreviewListDTO.builder()
                .heartResultList(heartResultList)
                .build();
    }
}
