package com.umc.lifesharing.payment.service;


import com.umc.lifesharing.payment.dto.TossPaymentDto;
import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import com.umc.lifesharing.payment.entity.TossPayment;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;

public interface PaymentCommandService {
    public TossPayment requestTossPayment(TossPaymentDto tossPaymentDto, Long userId, Long productId);
    public TossPaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws IOException, ParseException;
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) throws IOException, ParseException;
    public void tossPaymentFail(String code, String message, String orderId);
}
