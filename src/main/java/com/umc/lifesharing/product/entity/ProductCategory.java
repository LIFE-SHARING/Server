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
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'EXIST'")
    private ProductStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();
}
