package com.umc.lifesharing.reservation.entity;

public enum Method {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAYMENT("간편결제"),
    PHONE("휴대폰"),
    ACCOUNT_TRANSFER("계좌이체"),
    CULTURAL_GIFT_CERTIFICATE("문화상품권"),
    BOOK_GIFT_CERTIFICATE("도서문화상품권"),
    GAME_GIFT_CERTIFICATE("게임문화상품권");

    private final String description;

    Method(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
