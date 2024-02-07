package com.umc.lifesharing.reservation.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.reservation.converter.ReservationConverter;
import com.umc.lifesharing.reservation.dto.ReservationResponseDto;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.service.ReservationCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationRestController {

    private final ReservationCommandService reservationCommandService;

    @GetMapping("/list")
    @Operation(summary = "예약 목록 필터 조회 API",description = "예약 목록을 필터를 통해 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "filter", description = "필터 종류(default = 전체), Query Params 입니다! 현재는 전체, MY 두가지 필터만 사용가능합니다."),
    })
    public ApiResponse<ReservationResponseDto.ReservationPreViewListDTO> getReservationList(@AuthenticationPrincipal UserAdapter userAdapter, @RequestParam(name = "filter", defaultValue = "전체", required = false) String filter) {
        List<Reservation> reservationList = reservationCommandService.getReservationList(userAdapter, filter);

        return ApiResponse.onSuccess(ReservationConverter.toReservationListDto(reservationList, userAdapter.getUser().getId(), filter));
    }


}
