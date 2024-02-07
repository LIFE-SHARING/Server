package com.umc.lifesharing.payment.service;


import com.umc.lifesharing.payment.dto.TossPaymentDto;
import com.umc.lifesharing.payment.dto.TossPaymentPointDto;
import com.umc.lifesharing.payment.dto.TossPaymentReqDto;
import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface PaymentCommandService {
    public TossPaymentReqDto.TossPaymentResDto requestTossPaymentReservation(TossPaymentDto tossPaymentDto, Long userId, Long productId);
    public TossPaymentReqDto.TossPaymentResDto requestTossPaymentPoint(TossPaymentPointDto.ChargePointRequestDto tossPaymentDto, Long userId);
    public TossPaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws IOException, ParseException;
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) throws IOException, ParseException;
    public void tossPaymentFail(String code, String message, String orderId);
}
