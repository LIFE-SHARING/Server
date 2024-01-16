package com.umc.lifesharing.apiPayload.exception.handler;


import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class ProductHandler extends GeneralException {
    public ProductHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
