package com.umc.lifesharing.location.entity;

import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 위치는 한 가지만 가질 수 있기 때문에 OneToOne이지만 추후 여러 값을 가질 경우를 대비하여 1:N으로 엮음
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
