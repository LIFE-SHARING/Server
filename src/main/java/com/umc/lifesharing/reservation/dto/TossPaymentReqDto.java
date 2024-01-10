package com.umc.lifesharing.reservation.dto;

import com.umc.lifesharing.reservation.entity.Method;
import com.umc.lifesharing.reservation.entity.TossPayment;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossPaymentReqDto { // 실제 토스페이먼츠에 결제 요청 할 때 사용.

    private String payType;
    private Long amount;
    private String orderName;
    private String orderId;
    private String customerEmail;
    private String customerName;
    private String successUrl;
    private String failUrl;

    private String failReason;
    private boolean cancelYN;
    private String cancelReason;
    private String createdAt;
}
