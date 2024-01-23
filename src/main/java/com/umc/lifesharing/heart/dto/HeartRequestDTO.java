package com.umc.lifesharing.heart.dto;

import lombok.Getter;
import lombok.Setter;

public class HeartRequestDTO {

    @Getter
    @Setter
    public static class create{
        Long userId;
        Long productId;
    }
}
