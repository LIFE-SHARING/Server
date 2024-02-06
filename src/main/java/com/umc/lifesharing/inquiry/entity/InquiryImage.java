package com.umc.lifesharing.inquiry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id", nullable = false)
    @JsonIgnore
    private Inquiry inquiry;
}
