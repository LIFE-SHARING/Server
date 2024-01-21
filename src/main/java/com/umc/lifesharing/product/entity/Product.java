package com.umc.lifesharing.product.entity;

import com.umc.lifesharing.product.entity.common.BaseEntity;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    private Integer dayPrice;

    private Integer hourPrice;

    private Integer deposit;

    private String lendingPeriod;

    @Column(columnDefinition = "integer default 0")
    @Max(5)
    private Integer score;

    @Column(columnDefinition = "integer default 0")
    private Integer reviewCount;

    @Column(nullable = false, length = 200)
    private String content;

    private Boolean isPick;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'EXIST'")
    private ProductStatus product_status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductImage> images = new ArrayList<>();   //이미지를 리스트 형태로 받아와야함

    //임시대책
//    @ElementCollection
//    private List<String> image_url;

    public void setUser(User user){
        this.user = user;
    }

    public void setCategory(ProductCategory category){
        this.category = category;
    }

}
