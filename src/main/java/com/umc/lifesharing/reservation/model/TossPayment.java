//package com.umc.lifesharing.reservation.model;
//
//import com.umc.lifesharing.reservation.model.common.BaseEntity;
//import com.umc.lifesharing.user.entity.User;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//public class TossPayment extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "payment_id", nullable = false, unique = true)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Column(nullable = false, name = "method")
//    @Enumerated(EnumType.STRING)
//    private Method method;
//    @Column(nullable = false, name = "pay_amount")
//    private Long amount;
//    @Column(nullable = false, name = "pay_name")
//    private String orderName;
//    @Column(nullable = false, name = "order_id")
//    private String orderId;
//
//    @Column(name = "is_succeed")
//    private boolean isSucceed;
//    @Column(name = "payment_key")
//    private String paymentKey;
//    @Column(name = "fail_resion")
//    private String failReason;
//    @Column(name = "is_canceled")
//    private boolean isCanceled;
//    @Column(name = "cancel_reason")
//    private String cancelReason;
//
//}
