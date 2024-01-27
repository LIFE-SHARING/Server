package com.umc.lifesharing.reservation.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.entity.enum_class.Status;
import com.umc.lifesharing.reservation.repository.ReservationRepository;
import com.umc.lifesharing.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationCommandServiceImpl implements ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    @Override // 필터를 통한 대여 목록 조회
    public List<Reservation> getReservationList(UserAdapter userAdapter, String filter) {
        List<Product> productList = productRepository.findAllByUser(userAdapter.getUser());
        List<Reservation> reservationList = null;
        if(filter.equals("전체")){
            reservationList = reservationRepository.findAllByUserAndStatusOrProductIn(userAdapter.getUser(), Status.ACTIVE, productList);
        }else if (filter.equals("MY")){
            reservationList = reservationRepository.findAllByProductInAndStatus(productList, Status.ACTIVE);
        }


        return reservationList;
    }

}
