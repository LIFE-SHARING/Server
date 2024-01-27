package com.umc.lifesharing.apiPayload.exception.handler;

import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.exception.GeneralException;

public class ProductCategoryHandler extends GeneralException {
    public ProductCategoryHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
