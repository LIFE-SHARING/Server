package com.umc.lifesharing.apiPayload.code.status;


import com.umc.lifesharing.apiPayload.code.BaseErrorCode;
import com.umc.lifesharing.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 알수 없는 에러
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST,"UNKNOWN_ERROr","알 수 없는 에러입니다."),

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // 결제 관련 에러
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT4001", "결제 불가능한 금액입니다."),
    PAYMENT_AMOUNT_EXP(HttpStatus.BAD_REQUEST, "PAYMENT4002", "결제 금액이 일치하지 않습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4003", "결제 내역을 찾을 수 없습니다."),
    ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "PAYMENT4004", "이미 인증된 결제입니다."),
    ORDER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4003", "주문 번호를 찾을 수 없습니다."),

    CHARGE_POINT(HttpStatus.BAD_REQUEST, "PAYMENT4101", "캐쉬가 부족합니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT4001", "해당 제품이 없습니다."),
    NO_PRODUCT_IMAGE(HttpStatus.NOT_FOUND, "PRODUCT4002", "제품 이미지가 없습니다."),
    NOT_EXIST_LIKED_PRODUCT(HttpStatus.NOT_FOUND,"PRODUCT4003", "찜한 제품이 없습니다."),
    ALREADY_HEART(HttpStatus.ALREADY_REPORTED, "HEART4001", "이미 좋아요한 제품입니다."),
    ALREADY_REMOVE(HttpStatus.ALREADY_REPORTED, "HEART4002", "이미 취소한 제품입니다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW4001", "존재하지 않는 리뷰입니다."),
  
    // 예시,,,
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // Ror test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // FoodCategory Error
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4001", "해당 카테고리가 없습니다."),

    // Store Error

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_4001","가게가 없습니다."),
    // User
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "USER_400_1", "이미 존재하는 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_400_2", "사용자의 비밀번호가 잘못되었습니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "USER_400_3", "사용자의 로그인 타입이 잘못되었습니다. 소셜 로그인을 이용해주세요"),
    USER_ALREADY_SUBSCRIBED(HttpStatus.BAD_REQUEST, "USER_400_4", "이미 가입된 사용자입니다."),


    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "USER_404_1", "사용자를 찾을 수 없습니다."),

    // token
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN_400_1", "헤더에 토큰이 존재하지 않음"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_401_1", "토큰이 만료됨"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN_401_2", "토큰이 유효하지 않음");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}