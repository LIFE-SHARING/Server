package com.umc.lifesharing.reservation.converter;

import com.umc.lifesharing.reservation.dto.ReservationResponseDto;
import com.umc.lifesharing.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReservationConverter {

    public static ReservationResponseDto.ReservationResultDto reservationResultDto(Reservation reservation, Long userId, String filter){

        if(reservation.getProduct().getUser().getId().equals(userId)){
            filter = "MY";
        }else {
            filter = "대여";
        }

        return ReservationResponseDto.ReservationResultDto.builder()
                .reservationId(reservation.getId())
                .productId(reservation.getProduct().getId())
                .productName(reservation.getProduct().getName())
                .productImage(reservation.getProduct().getImageUrl())
                .filter(filter)
                .startDate(reservation.getStart_date())
                .endDate(reservation.getEnd_date())
                .location(null) // 아직 입력이 정해지지 않아 null로 설정
                .build();
    }

    public static ReservationResponseDto.ReservationPreViewListDTO toReservationListDto(List<Reservation> reservationList, Long userId, String filter){

        List<ReservationResponseDto.ReservationResultDto> reservationResultDtoList = reservationList.stream()
                .map(ReservationConverter.mapToReservationResultDto(filter, userId)).collect(Collectors.toList());

        return ReservationResponseDto.ReservationPreViewListDTO.builder()
                .appliedFilter(filter)
                .reservationList(reservationResultDtoList)
                .build();
    }

    public static Function<Reservation, ReservationResponseDto.ReservationResultDto> mapToReservationResultDto(String filter, Long userId) {
        return reservation -> reservationResultDto(reservation, userId, filter);
    }
}
