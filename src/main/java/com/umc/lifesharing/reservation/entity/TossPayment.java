package com.umc.lifesharing.reservation.entity;

import com.umc.lifesharing.reservation.entity.common.BaseEntity;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TossPayment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "method")
    @Enumerated(EnumType.STRING)
    private Method method;              // 결제 방법
    @Column(nullable = false, name = "pay_amount")
    private Long amount;                // 결제 금액
    @Column(nullable = false, name = "pay_name")
    private String orderName;           // 주문 이름
    @Column(nullable = false, name = "order_id")
    private String orderId;             // 주문 id

    @Column(name = "is_succeed")
    private boolean isSucceed;          // 성공 여부
    @Column(name = "payment_key")
    private String paymentKey;          // 결제 키
    @Column(name = "fail_resion")
    private String failReason;          // 실패 사유
    @Column(name = "is_canceled")
    private boolean isCanceled;         // 취소 여부
    @Column(name = "cancel_reason")
    private String cancelReason;        // 취소 사유

}
