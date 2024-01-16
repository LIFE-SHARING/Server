package com.umc.lifesharing.user.entity;

import jakarta.persistence.*;

@Entity
public class Terms {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String body;

    private Boolean requried;
}
