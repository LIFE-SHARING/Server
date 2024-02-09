package com.umc.lifesharing.payment.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.TossPaymentConfig;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.payment.dto.*;
import com.umc.lifesharing.payment.service.PaymentCommandService;
import com.umc.lifesharing.payment.service.PaymentQueryService;
import com.umc.lifesharing.product.validation.annotation.ExistProduct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("payments")
public class PaymentRestController {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;
    private final TossPaymentConfig tossPaymentConfig;

    @PostMapping("/{productId}/toss/reserve")
    @Operation(summary = "예약 등록 및 결제 정보 등록 API",description = "예약 내역을 등록하고 TossPay에 필요한 정보를 등록합니다. 최초로 결제하기를 눌렀을 때 사용.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "productId", description = "제품 아이디, path variable 입니다!"),
    })
    public ApiResponse<TossPaymentReqDto.TossReservationPaymentResDto> requestTossPaymentReservation(@AuthenticationPrincipal UserAdapter userAdapter,
                                                                                          @ExistProduct @PathVariable Long productId, @RequestBody @Valid TossPaymentDto tossPaymentDto) {
        TossPaymentReqDto.TossReservationPaymentResDto paymentResDto = paymentCommandService.requestTossPaymentReservation(tossPaymentDto, userAdapter.getUser().getId(), productId);
//        paymentResDto.setSuccessUrl(tossPaymentDto.getYourSuccessUrl() == null ? tossPaymentConfig.getSuccessUrl() : tossPaymentDto.getYourSuccessUrl());
//        paymentResDto.setFailUrl(tossPaymentDto.getYourFailUrl() == null ? tossPaymentConfig.getFailUrl() : tossPaymentDto.getYourFailUrl());
        paymentResDto.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
        paymentResDto.setFailUrl(tossPaymentConfig.getFailUrl());
        return ApiResponse.onSuccess(paymentResDto);
    }

    @PostMapping("/toss/point")
    @Operation(summary = "포인트 결제 API",description = "포인트 충전용 API.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<TossPaymentReqDto.TossPaymentResDto> requestTossPaymentPoint(@AuthenticationPrincipal UserAdapter userAdapter, @RequestBody @Valid TossPaymentPointDto.ChargePointRequestDto tossPaymentDto) {
        TossPaymentReqDto.TossPaymentResDto paymentResDto = paymentCommandService.requestTossPaymentPoint(tossPaymentDto, userAdapter.getUser().getId());

        paymentResDto.setSuccessUrl(tossPaymentConfig.getSuccessUrl());
        paymentResDto.setFailUrl(tossPaymentConfig.getFailUrl());
        return ApiResponse.onSuccess(paymentResDto);
    }

    @GetMapping("/toss/success")
    public ApiResponse<TossPaymentSuccessDto> tossPaymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) throws IOException, ParseException {
        System.out.println("Controller");
        return ApiResponse.onSuccess(paymentCommandService.tossPaymentSuccess(paymentKey, orderId, amount));
    }

    @PostMapping("/toss/fail")
    public ApiResponse<TossPaymentFailDto> tossPaymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId
    ) {
        paymentCommandService.tossPaymentFail(code, message, orderId);
        return ApiResponse.onSuccess(TossPaymentFailDto.builder()
                .errorCode(code)
                .errorMessage(message)
                .orderId(orderId)
                .build());
    }
}
