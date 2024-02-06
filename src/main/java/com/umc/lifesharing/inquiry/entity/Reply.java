package com.umc.lifesharing.inquiry.entity;

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

    @OneToOne(mappedBy = "reply", fetch = FetchType.LAZY)
    private Inquiry inquiry;
}
