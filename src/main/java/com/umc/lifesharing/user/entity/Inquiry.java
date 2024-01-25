package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inquiry_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = true)
    private List<String> imageUrlList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;
}
