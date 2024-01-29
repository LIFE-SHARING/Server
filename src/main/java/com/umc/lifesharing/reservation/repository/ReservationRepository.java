package com.umc.lifesharing.reservation.repository;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.entity.enum_class.Status;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByIdAndStatus(Long id, Status status);

    Optional<Reservation> findByOrderId(String orderId);

    List<Reservation> findAllByUserAndStatusOrProductIn(User userId, Status status, List<Product> productList);

    List<Reservation> findAllByProductInAndStatus(List<Product> productList, Status status);

    boolean existsReservationByProductAndStartDateAndEndDate(Product product, LocalDateTime startDate, LocalDateTime endDate);
}
