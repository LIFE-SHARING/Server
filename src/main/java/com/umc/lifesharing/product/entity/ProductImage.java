package com.umc.lifesharing.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static ProductImage create(String imageUrl) {
        return ProductImage.builder()
                .imageUrl(imageUrl)
                .build();
    }

    // 기본 생성자
    public ProductImage() {
    }

    // 이미지를 받는 생성자
    public ProductImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
