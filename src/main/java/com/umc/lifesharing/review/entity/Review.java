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
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product_name;

    @Column(length = 200)
    private String content;

    @Max(value = 5)
    private Float score;

    private Integer lent_day;

    @ElementCollection
    private List<String> image_url;

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
