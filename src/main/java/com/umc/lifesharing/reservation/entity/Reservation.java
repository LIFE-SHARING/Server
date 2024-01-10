package com.umc.lifesharing.reservation.entity;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.reservation.entity.common.BaseEntity;
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
    private LocalDateTime startDate;        // 예약 시작일

    @Column(nullable = false, name = "end_date")
    private LocalDateTime end_date;         // 예약 종료일

    @Column(nullable = false, name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType payment_type;       // 결제 유형

    @Column(nullable = false, name = "amount")
    private Long amount;                    // 결제 금액 (보증금 제외)

    @Column(nullable = false, name = "deposit")
    private Long deposit;                   // 보증금


}
