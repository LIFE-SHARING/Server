package com.umc.lifesharing.review.entity;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.review.entity.common.BaseEntity;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String content;

    @Max(value = 5)
    private Integer score;

    private Integer lentDay;

    @ElementCollection
    @CollectionTable(name = "review_image_url", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public void setUser(User user){
        this.user = user;
    }

    public void setProduct(Product product){
        this.product = product;
    }


}
