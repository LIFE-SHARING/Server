package com.umc.lifesharing.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossPaymentFailDto {
    String errorCode;
    String errorMessage;
    String orderId;
}
