package com.umc.lifesharing.user.entity;

import jakarta.persistence.*;

@Entity
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String receiverPhone;
    private String postCode;
    private String address;
}
