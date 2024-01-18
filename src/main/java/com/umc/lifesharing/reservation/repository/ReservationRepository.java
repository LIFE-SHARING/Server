package com.umc.lifesharing.reservation.repository;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long id);

    List<Reservation> findAllByIdOrProductIn(Long id, List<Product> productList);
}
