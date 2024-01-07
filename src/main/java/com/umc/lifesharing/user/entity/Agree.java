package com.umc.lifesharing.user.entity;

import jakarta.persistence.*;

@Entity
public class Agree {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "terms_id")
    private Terms terms;
}
