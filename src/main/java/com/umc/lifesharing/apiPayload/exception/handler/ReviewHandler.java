package com.umc.lifesharing.apiPayload.exception.handler;

import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class ReviewHandler extends GeneralException {
    public ReviewHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
