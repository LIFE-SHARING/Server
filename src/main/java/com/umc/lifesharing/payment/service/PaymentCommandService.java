package com.umc.lifesharing.payment.service;


import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import com.umc.lifesharing.payment.entity.TossPayment;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;

public interface PaymentCommandService {
    public TossPayment requestTossPayment(TossPayment payment, Long userId);
    public TossPaymentSuccessDto tossPaymentSuccess(String paymentKey, String orderId, Long amount) throws IOException, ParseException;
    public TossPaymentSuccessDto requestPaymentAccept(String paymentKey, String orderId, Long amount) throws IOException, ParseException;
}
