package com.umc.lifesharing.reservation.service;


import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.user.entity.User;

import java.util.List;

public interface ReservationCommandService {
    List<Reservation> getReservationList(UserAdapter userAdapter, String filter);
}
