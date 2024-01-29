package com.umc.lifesharing.apiPayload.code.status;

import com.umc.lifesharing.apiPayload.code.BaseCode;
import com.umc.lifesharing.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 채팅 관련 응답
    CHAT_OUT_SENDER(HttpStatus.OK, "CHATROOM_200_1", "구매자가 채팅방을 나갔습니다."),
    CHAT_OUT_RECEIVER(HttpStatus.OK, "CHATROOM_200_2", "판매자가 채팅방을 나갔습니다.");

    // 멤버 관련 응답

    // ~~~ 관련 응답

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}