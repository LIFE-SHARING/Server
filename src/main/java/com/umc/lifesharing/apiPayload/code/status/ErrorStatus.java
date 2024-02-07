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
    UNKNOWN_ERROR(HttpStatus.BAD_REQUEST,"UNKNOWN_ERROR","알 수 없는 에러입니다."),

    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // 결제 관련 에러
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT4001", "결제 불가능한 금액입니다."),
    PAYMENT_AMOUNT_EXP(HttpStatus.BAD_REQUEST, "PAYMENT4002", "결제 금액이 일치하지 않습니다."),
    PAYMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4003", "결제 내역을 찾을 수 없습니다."),
    ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "PAYMENT4004", "이미 인증된 결제입니다."),
    ORDER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAYMENT4005", "주문 번호를 찾을 수 없습니다."),

    CHARGE_POINT(HttpStatus.BAD_REQUEST, "PAYMENT4101", "캐쉬가 부족합니다."),

    //예약 관련 에러
    CHECK_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "RESERVATION4001", "예약 날짜를 다시 확인해 주세요."),
    INVALID_RESERVATION_DATE(HttpStatus.BAD_REQUEST, "RESERVATION4002", "예약 불가능한 날짜입니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT4001", "해당 제품이 없습니다."),
    NO_PRODUCT_IMAGE(HttpStatus.NOT_FOUND, "PRODUCT4002", "제품 이미지가 없습니다."),
    NOT_EXIST_LIKED_PRODUCT(HttpStatus.NOT_FOUND,"PRODUCT4003", "찜한 제품이 없습니다."),
    ALREADY_HEART(HttpStatus.ALREADY_REPORTED, "HEART4001", "이미 좋아요한 제품입니다."),
    ALREADY_REMOVE(HttpStatus.ALREADY_REPORTED, "HEART4002", "이미 취소한 제품입니다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW4001", "존재하지 않는 리뷰입니다."),
    NOT_REVIEWLIST(HttpStatus.NOT_FOUND, "REVIEW4002", "리뷰가 없습니다."),
  
    // 예시,,,
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // Ror test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // FoodCategory Error
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY4001", "해당 카테고리가 없습니다."),

    // Store Error

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_4001","가게가 없습니다."),
    // User
    DUPLICATED_EMAIL_OR_NICKNAME(HttpStatus.BAD_REQUEST, "USER_400_1", "이메일 혹은 닉네임이 중복입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER_400_2", "사용자의 비밀번호가 잘못되었습니다."),
    INVALID_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "USER_400_3", "사용자의 로그인 타입이 잘못되었습니다. 소셜 로그인을 이용해주세요"),
    USER_ALREADY_SUBSCRIBED(HttpStatus.BAD_REQUEST, "USER_400_4", "이미 가입된 사용자입니다."),

    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "USER_404_1", "사용자를 찾을 수 없습니다."),
    DO_NOT_CHANGE_PRODUCT(HttpStatus.BAD_REQUEST, "USER_400_5", "제품을 수정할 수 없습니다."),

    // chat
    CHATROOM_NOT_EXIST(HttpStatus.NOT_FOUND, "CHATROOM_400_1", "채팅방을 찾을 수 없습니다."),
    CHAT_NOT_AVAILABLE_SENDER(HttpStatus.NOT_FOUND, "CHATROOM_400_2", "구매자가 채팅방을 나갔습니다."),
    CHAT_NOT_AVAILABLE_RECEIVER(HttpStatus.NOT_FOUND, "CHATROOM_400_3", "판매자가 채팅방을 나갔습니다."),
    CHAT_NOT_MATCH_SENDER(HttpStatus.NOT_FOUND, "CHATROOM_400_4", "구매자가 일치하지 않습니다."),
    CHAT_NOT_MATCH_RECEIVER(HttpStatus.NOT_FOUND, "CHATROOM_400_5", "판매자가 일치하지 않습니다."),
    CHATROOM_NULL_SENDER(HttpStatus.BAD_REQUEST, "CHATROOM_400_6", "구매자가 이미 채팅방을 나갔습니다."),
    CHATROOM_NULL_RECEIVER(HttpStatus.BAD_REQUEST, "CHATROOM_400_7", "판매자가 이미 채팅방을 나갔습니다."),
    // token
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN_400_1", "헤더에 토큰이 존재하지 않음"),
    TOKEN_UNABLE_TO_EXTRACT(HttpStatus.BAD_REQUEST, "TOKEN_400_2", "토큰을 추출할 수 없음"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_401_1", "토큰이 만료됨"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "TOKEN_401_2", "토큰이 유효하지 않음"),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "TOKEN_401_3", "지원하지 않는 토큰 타입임"),


    PAGE_INVALID(HttpStatus.BAD_REQUEST, "PAGE_400_1", "페이지는 1이상입니다!"),

    LOCATION_VALUE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LOCATION_400_1", "사용자의 인증된 위치 정보를 찾을 수 없습니다."),

    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTICEE_400_1","공지를 찾을 수 없습니다."),

    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "INQUIRY_400_1","문의를 찾을 수 없습니다.");


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