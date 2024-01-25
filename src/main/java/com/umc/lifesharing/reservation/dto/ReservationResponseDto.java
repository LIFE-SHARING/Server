package com.umc.lifesharing.reservation.dto;

import com.umc.lifesharing.product.validation.annotation.ExistProduct;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationResultDto{
        private Long reservationId;
        @ExistProduct
        private Long productId; //제품 번호

        private String productName; //제품명
        private String productImage; //제품 이미지
        private String filter; // 적용될 필터
        private LocalDateTime startDate; // 대여 시작일
        private LocalDateTime endDate; // 대여 종료일
        private String location; // 위치 -> 추후에 설정 일단 null
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationPreViewListDTO{
        List<ReservationResponseDto.ReservationResultDto> reservationList;
        String appliedFilter;

    }


}
