package com.umc.lifesharing.alarm.entity;

import com.umc.lifesharing.alarm.AlarmType;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private AlarmType alarmType;

    private Boolean readOrNot;

    private String title;

    private String body;
}
