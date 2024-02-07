package com.umc.lifesharing.apiPayload.exception.handler;


import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class PaymentHandler extends GeneralException {
    public PaymentHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
