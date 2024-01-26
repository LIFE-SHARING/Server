package com.umc.lifesharing.review.entity;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column
    private String fullImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;


    public static ReviewImage create(String imageUrl) {
        return ReviewImage.builder()
                .imageUrl(imageUrl)
                .build();
    }

    // 정적 팩토리 메서드
    public static ReviewImage create(Review review, String imageUrl, String fullImageUrl) {
        ReviewImage reviewImage = new ReviewImage();
        reviewImage.setReview(review);
        reviewImage.setImageUrl(imageUrl);
        reviewImage.setFullImageUrl(fullImageUrl);
        return reviewImage;
    }
}
