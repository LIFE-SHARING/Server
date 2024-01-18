package com.umc.lifesharing.reservation.service;


import com.umc.lifesharing.reservation.dto.ReservationListDto;
import com.umc.lifesharing.user.entity.User;

import java.util.List;

public interface ReservationCommandService {
    List<ReservationListDto> getReservationList(User user, String filter);
}
