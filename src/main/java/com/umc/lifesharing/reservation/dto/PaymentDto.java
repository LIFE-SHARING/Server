package com.umc.lifesharing.reservation.dto;

import com.umc.lifesharing.reservation.model.Method;
import com.umc.lifesharing.reservation.model.TossPayment;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    @NonNull
    private Method payType;
    @NonNull
    private Long amount;
    @NonNull
    private String orderName;

    private String yourSuccessUrl;
    private String yourFailUrl;


    public TossPayment toEntity() {
        return TossPayment.builder()
                .method(payType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .isSucceed(false)
                .build();
    }
}
