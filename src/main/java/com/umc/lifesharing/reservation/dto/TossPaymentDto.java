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
public class TossPaymentDto { // 프론트에서 입력받을 때 사용

    @NonNull
    private Method payType;
    @NonNull
    private Long amount;
    @NonNull
    private String orderName;

    private String yourSuccessUrl;
    private String yourFailUrl;
    private boolean isSucceed;


    public TossPayment toEntity() {
        return TossPayment.builder()
                .method(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .isSucceed(isSucceed)
                .build();
    }
}
