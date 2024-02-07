package com.umc.lifesharing.apiPayload.exception.handler;

import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
