package com.umc.lifesharing.user.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Entity
public class UserServiceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate joinDate;

    private LocalDate passwdUpdateDate;

    private LocalDate inactiveDate;

    private LocalDate accessDate;

}
