package com.umc.lifesharing.apiPayload.exception.handler;


import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class ReservationHandler extends GeneralException {
    public ReservationHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
