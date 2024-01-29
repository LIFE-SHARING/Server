package com.umc.lifesharing.apiPayload.exception.handler;

import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class ChatHandler extends GeneralException {
    public ChatHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
