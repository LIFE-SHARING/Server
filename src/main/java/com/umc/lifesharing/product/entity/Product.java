package com.umc.lifesharing.product.entity;

import com.umc.lifesharing.product.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;          // 제품 이름

    @Column
    private Integer percent;      // 할인율

    @Column
    private Integer price;        // 원가

    @Column
    private Integer day_price;    // 일별 대여비

    @Column
    private Integer hour_price;    // 시간별 대여비

    @Column
    private Integer deposit;     // 보증금

    @Column
    private String lending_period;  // 대여 기간

    @Column
    private String least_lent;  // 최소 대여기간

    @Column
    private Float score;          // 별점

    @Column
    private Integer score_count;  // 별점 개수

    @Column
    private Boolean is_pick;      // 찜 여부

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'NOTEXIST'")
    private ProductStatus status;  // 상태

}
