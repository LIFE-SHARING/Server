package com.umc.lifesharing.payment.dto;

import com.umc.lifesharing.payment.entity.enum_class.PaymentType;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

public class TossPaymentReqDto {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossReservationPaymentResDto {// 실제 토스페이먼츠에 결제 요청 할 때 사용. (예약용)
        private Long reservationId;
        private String method; // 결제 방식 ex) CARD, VIRTUAL_ACCOUNT ...
        private PaymentType paymentType; // 결제 유형 ex) 제품 예약, 포인트
        private Long amount;
        private String orderName;
        private String orderId;
        private String userEmail;
        private String userName;
        private String successUrl;
        private String failUrl;

        private String failReason;
        private boolean cancelYN;
        private String cancelReason;
        private String createdAt;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TossPaymentResDto {// 실제 토스페이먼츠에 결제 요청 할 때 사용.
        private String method; // 결제 방식 ex) CARD, VIRTUAL_ACCOUNT ...
        private PaymentType paymentType; // 결제 유형 ex) 제품 예약, 포인트
        private Long amount;
        private String orderName;
        private String orderId;
        private String userEmail;
        private String userName;
        private String successUrl;
        private String failUrl;

        private String failReason;
        private boolean cancelYN;
        private String cancelReason;
        private String createdAt;
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyReqDto {// 실제 토스페이먼츠에 결제 요청 할 때 사용.

        private String paymentKey;
        private String orderId;
        private Long amount;
        private PaymentType paymentType;
    }
}
