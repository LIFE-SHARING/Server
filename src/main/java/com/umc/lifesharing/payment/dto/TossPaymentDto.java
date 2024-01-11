package com.umc.lifesharing.payment.dto;

import com.umc.lifesharing.reservation.entity.enum_class.Method;
import com.umc.lifesharing.payment.entity.TossPayment;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossPaymentDto { // 프론트에서 입력받을 때 사용

    @NonNull
    private Method method; // 결제 방법
    @NonNull
    private Long amount; // 결제 금액
    @NonNull
    private Long deposit; // 보증금
    @NonNull
    private String orderName; // 주문명

    private String yourSuccessUrl; // 성공시 반환 URL
    private String yourFailUrl; // 실패시 반환 URL
    private boolean isSucceed; // 성공여부


    public TossPayment toEntity() {
        return TossPayment.builder()
                .method(method)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .isSucceed(isSucceed)
                .build();
    }
}
