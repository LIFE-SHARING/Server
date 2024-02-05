package com.umc.lifesharing.apiPayload.exception.handler;

import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {
    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
