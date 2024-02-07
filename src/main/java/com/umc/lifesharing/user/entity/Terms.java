package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Deprecated
public class Terms extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String body;

    private Boolean requried;
}
