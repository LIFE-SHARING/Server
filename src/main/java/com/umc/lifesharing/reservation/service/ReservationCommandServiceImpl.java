package com.umc.lifesharing.reservation.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.PaymentHandler;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.payment.entity.TossPayment;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.reservation.dto.ReservationListDto;
import com.umc.lifesharing.reservation.entity.Reservation;
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
    public List<ReservationListDto> getReservationList(User user, String filter) {
        List<Product> productList = productRepository.findAllByUser(user);
        List<Reservation> reservationList = reservationRepository.findAllByIdOrProductIn(user.getId(), productList);
        TossPayment payment = tossPaymentDto.toEntity();
        if (payment.getAmount() < 1000) {
            throw new PaymentHandler(ErrorStatus.CHARGE_POINT);
        }
        // 결제 내역 생성
        Reservation reservation = Reservation.builder()
                .user(user)
                .product(product)
                .amount(tossPaymentDto.getAmount())
                .deposit(tossPaymentDto.getDeposit())
                .start_date(tossPaymentDto.getStartDate())
                .end_date(tossPaymentDto.getEndDate())
                .total_time(tossPaymentDto.getTotalTime())
                .build();
        reservationRepository.save(reservation);

        payment.setUser(user);
        payment.setAmount(tossPaymentDto.getAmount() + tossPaymentDto.getDeposit());
        return tossPaymentRepository.save(payment);
    }

}
