package com.umc.lifesharing.payment.service;


import com.umc.lifesharing.payment.entity.TossPayment;

public interface PaymentCommandService {
    public TossPayment requestTossPayment(TossPayment payment, Long userId);
}
