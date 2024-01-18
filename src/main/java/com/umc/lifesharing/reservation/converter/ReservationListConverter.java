package com.umc.lifesharing.reservation.converter;

import com.umc.lifesharing.reservation.dto.ReservationListDto;
import com.umc.lifesharing.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import umc.spring.domain.PreferCategory;
import umc.spring.domain.RestaurantCategory;
import umc.spring.domain.User;
import umc.spring.web.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReservationListConverter {

    public static ReservationListDto.ReservationResultDto toReservationResultDto(Reservation reservation, String filter){
        return ReservationListDto.ReservationResultDto.builder()
                .productId(reservation.getProduct().getId())
                .productName(reservation.getProduct().getName())
                .productImage(reservation.getProduct().getImageUrl())
                .filter(filter)
                .startDate(reservation.getStart_date())
                .endDate(reservation.getEnd_date())
                .location(null)
                .build();
    }

//    public static List<Reservation> toUserPreferList(User user, List<Reservation> restaurantCategoryList){
//
//        return restaurantCategoryList.stream()
//                .map(foodCategory ->
//                        PreferCategory.builder()
//                                .restaurantCategory(foodCategory)
//                                .build()
//                ).collect(Collectors.toList());
//    }
}
