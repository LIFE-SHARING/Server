package com.umc.lifesharing.reservation.entity;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.common.BaseEntity;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, name = "start_date")
    private LocalDateTime start_date;

    @Column(nullable = false, name = "end_date")
    private LocalDateTime end_date;

    @Column(nullable = false, name = "total_time")
    private String total_time;       // 총 대여 시간

    @Column(nullable = false, name = "amount")
    private Long amount;

    @Column(nullable = false, name = "deposit")
    private Long deposit;


}
