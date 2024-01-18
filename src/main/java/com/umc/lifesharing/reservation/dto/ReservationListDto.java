package com.umc.lifesharing.reservation.dto;

import com.umc.lifesharing.product.validation.annotation.ExistProduct;
import lombok.*;

import java.time.LocalDateTime;

public class ReservationListDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationResultDto{
        @ExistProduct
        private Long productId; //제품 번호

        private String productName; //제품명
        private String productImage; //제품 이미지
        private String filter; // 적용될 필터
        private String startDate; // 대여 시작일
        private String endDate; // 대여 종료일
        private String location; // 위치 -> 추후에 설정 일단 null
    }


}
