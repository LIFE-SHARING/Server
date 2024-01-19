package com.umc.lifesharing.payment.dto;

import com.umc.lifesharing.payment.entity.enum_class.Method;
import com.umc.lifesharing.product.validation.annotation.ExistProduct;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class TossPaymentPointDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChargePointRequestDto{
        @NonNull
        @Schema(description = "결제 방법 enum이기 때문에 example에 있는 데이터만 입력할 수 있습니다." , nullable = false, example = "CARD, VIRTUAL_ACCOUNT, EASY_PAYMENT, PHONE,\n" +
                "ACCOUNT_TRANSFER, CULTURAL_GIFT_CERTIFICATE, BOOK_GIFT_CERTIFICATE, GAME_GIFT_CERTIFICATE")
        private Method method; // 결제 방법
        @NonNull
        @Schema(description = "결제 금액" ,nullable = false, example = "10000")
        private Long amount; // 결제 금액
        @NonNull
        @Schema(description = "주문명" ,nullable = false, example = "Canon [렌즈 포함] EOS R8CanonCanon\n[렌즈 포함] EOS R8CanonCanon")
        private String orderName; // 주문명

        @Schema(description = "성공시 반환 URL" ,nullable = true, example = "서버에서 자동으로 입력되는 값입니다.(입력X)")
        private String yourSuccessUrl; // 성공시 반환 URL
        @Schema(description = "실패시 반환 URL" ,nullable = true, example = "서버에서 자동으로 입력되는 값입니다.(입력X)")
        private String yourFailUrl; // 실패시 반환 URL
        @Schema(description = "성공여부" ,nullable = true, example = "서버에서 자동으로 입력되는 값입니다.(입력X)")
        private boolean isSucceed; // 성공 여부
    }
}
