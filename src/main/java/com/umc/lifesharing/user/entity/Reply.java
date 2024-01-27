package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String body;
}
