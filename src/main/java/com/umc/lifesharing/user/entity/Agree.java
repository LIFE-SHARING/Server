package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;

@Entity
public class Agree extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id")
    private Terms terms;
}
