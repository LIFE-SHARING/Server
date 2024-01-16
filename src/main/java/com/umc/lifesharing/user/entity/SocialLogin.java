package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.user.entity.enum_class.SocialType;
import jakarta.persistence.*;

@Entity
public class SocialLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String externalId;

    private String accessToken;
}
