package com.umc.lifesharing.heart.converter;

import com.umc.lifesharing.heart.dto.HeartResponseDTO;
import com.umc.lifesharing.heart.entity.Heart;

import java.time.LocalDate;

public class HeartConverter {

    public static HeartResponseDTO.createResult toCreateHeart(Heart heart){
        return HeartResponseDTO.createResult.builder()
                .productId(heart.getProduct().getId())
                .createdAt(LocalDate.now())
                .build();
    }

    public static HeartResponseDTO.removeResult toRemoveHeart(Heart heart){
        return HeartResponseDTO.removeResult.builder()
                .productId(heart.getProduct().getId())
                .updatedAt(LocalDate.now())
                .build();
    }
}
