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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String fullImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public static ProductImage create(String imageUrl) {
        return ProductImage.builder()
                .imageUrl(imageUrl)
//                .fullImageUrl(fullImageUrl)
                .build();
    }

    // 정적 팩토리 메서드
    public static ProductImage create(Product product, String imageUrl, String fullImageUrl) {
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImageUrl(imageUrl);
        productImage.setFullImageUrl(fullImageUrl);
        return productImage;
    }

    // 기본 생성자
    public ProductImage() {
    }

 
}
