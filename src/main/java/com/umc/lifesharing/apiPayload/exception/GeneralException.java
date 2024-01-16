package com.umc.lifesharing.apiPayload.exception;

import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

//    public GeneralException(BaseErrorCode errorCode) {
//        super(errorCode.getReason().getMessage());
//        this.code = errorCode;
//    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}