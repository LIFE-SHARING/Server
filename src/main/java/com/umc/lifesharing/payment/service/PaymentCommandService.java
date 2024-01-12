package com.umc.lifesharing.payment.service;


import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import com.umc.lifesharing.payment.entity.TossPayment;

public interface PaymentCommandService {
    public TossPayment requestTossPayment(TossPayment payment, Long userId);
    public TossPaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount);
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount);
}
