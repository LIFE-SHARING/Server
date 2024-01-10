package com.umc.lifesharing.product.entity;

import com.umc.lifesharing.product.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
public class ProductCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;  // 카테고리 이름

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'EXIST'")
    private ProductStatus status;  // 카테고리 존재 여부

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Product> productList = new ArrayList<>();
}
