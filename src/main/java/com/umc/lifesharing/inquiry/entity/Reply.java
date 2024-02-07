package com.umc.lifesharing.inquiry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String body;

    @OneToOne
    @JsonIgnore
    private Inquiry inquiry;

    public void updateBody(String body) {
        this.body = body;
    }
}
