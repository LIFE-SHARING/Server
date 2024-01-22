package com.umc.lifesharing.payment.converter;

import com.umc.lifesharing.payment.dto.TossPaymentPointDto;
import com.umc.lifesharing.payment.dto.TossPaymentReqDto;
import com.umc.lifesharing.payment.entity.TossPayment;
import com.umc.lifesharing.payment.entity.enum_class.PaymentType;
import com.umc.lifesharing.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class TossPaymentConverter {
    public static TossPayment toTossPayment(TossPaymentPointDto.ChargePointRequestDto tossPaymentDto, User user, PaymentType paymentType){

        return TossPayment.builder()
                .user(user)
                .method(tossPaymentDto.getMethod())
                .paymentType(paymentType)
                .amount(tossPaymentDto.getAmount())
                .orderName(tossPaymentDto.getOrderName())
                .orderId(UUID.randomUUID().toString())
                .isSucceed(tossPaymentDto.isSucceed())
                .build();
    }

    public static TossPaymentReqDto.TossPaymentResDto toPaymentResDto(TossPayment tossPayment, PaymentType paymentType) {
        return TossPaymentReqDto.TossPaymentResDto.builder()
                .method(String.valueOf(tossPayment.getMethod()))
                .paymentType(paymentType)
                .amount(tossPayment.getAmount())
                .orderName(tossPayment.getOrderName())
                .orderId(tossPayment.getOrderId())
                .userEmail(tossPayment.getUser().getEmail())
                .userName(tossPayment.getUser().getName())
                .createdAt(String.valueOf(tossPayment.getCreatedAt()))
                .cancelYN(false)
                .failReason(tossPayment.getFailReason())
                .build();
    }
}
