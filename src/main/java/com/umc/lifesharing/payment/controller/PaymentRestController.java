package com.umc.lifesharing.payment.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.TossPaymentConfig;
import com.umc.lifesharing.payment.dto.TossPaymentDto;
import com.umc.lifesharing.payment.dto.TossPaymentReqDto;
import com.umc.lifesharing.payment.dto.TossPaymentSuccessDto;
import com.umc.lifesharing.payment.service.PaymentCommandService;
import com.umc.lifesharing.payment.service.PaymentQueryService;
import com.umc.lifesharing.reservation.service.ReservationCommandService;
import com.umc.lifesharing.reservation.service.ReservationQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("payments")
public class PaymentRestController {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;
    private final TossPaymentConfig tossPaymentConfig;

    @PostMapping("/{userId}/toss")
    public ApiResponse<TossPaymentReqDto> requestTossPayment(@PathVariable Long userId, @RequestBody @Valid TossPaymentDto tossPaymentDto) {
        TossPaymentReqDto paymentResDto = paymentCommandService.requestTossPayment(tossPaymentDto.toEntity(), userId).toPaymentResDto();
        paymentResDto.setSuccessUrl(tossPaymentDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : tossPaymentDto.getYourSuccessUrl());
        paymentResDto.setFailUrl(tossPaymentDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : tossPaymentDto.getYourFailUrl());
        return ApiResponse.onSuccess(paymentResDto);
    }

    @GetMapping("/toss/success")
    public ApiResponse<TossPaymentSuccessDto> tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {

        return ApiResponse.onSuccess(paymentCommandService.tossPaymentSuccess(paymentKey, orderId, amount));
    }
}
