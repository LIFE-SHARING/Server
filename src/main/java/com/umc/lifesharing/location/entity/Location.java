package com.umc.lifesharing.location.entity;

import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String latitude;    // 위도
    private String longitude;   // 경도
    private String city;    // 시/도
    private String local;   // 군/구
    private String area;    // 읍/면/동
    private String status;

}
