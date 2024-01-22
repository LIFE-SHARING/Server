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
    public TossPaymentSuccessDto tossPaymentSuccess(TossPaymentReqDto.VerifyReqDto verifyReqDto) throws IOException, ParseException;
    public TossPaymentSuccessDto requestPaymentAccept(TossPaymentReqDto.VerifyReqDto verifyReqDto) throws IOException, ParseException;
    public void tossPaymentFail(String code, String message, String orderId);
}
