package com.umc.lifesharing.heart.dto;

import com.umc.lifesharing.product.dto.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class HeartResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createResult{
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class removeResult{
        Long productId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class heartResult{
        Long productId;
        String name;
        String location;  //사용자로부터 위치 정보를 가져와야함
        Integer deposit;
        Integer dayPrice;
        Integer score;
        Integer reviewCount;
        String image_url;  //이미지 처리 방법 알아내기
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartPreviewListDTO{
        List<HeartResponseDTO.heartResult> heartResultList;
    }
}
