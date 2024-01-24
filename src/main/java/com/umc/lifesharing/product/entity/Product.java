package com.umc.lifesharing.product.entity;

import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.product.entity.common.BaseEntity;
import com.umc.lifesharing.product.entity.enums.RentStatus;
import com.umc.lifesharing.product.entity.enums.ProductStatus;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.annotations.*;

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

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'EXIST'")
    private ProductStatus productStatus;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'AVAILABLE'")
    private RentStatus rentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    //@Fetch(FetchMode.SELECT)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();   //이미지를 리스트 형태로 받아와야함

    @Builder.Default
    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private List<Reservation> reservationList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", orphanRemoval = true)
    private List<Heart> heartListList = new ArrayList<>();   

//    // 아마존 S3
//    @ElementCollection
//    @CollectionTable(name = "product_image_url", joinColumns = @JoinColumn(name = "product_id"))
//    @Column(name = "image_url")
//    private List<String> image_url;

    public void setUser(User user){
        this.user = user;
    }

    public void setCategory(ProductCategory category){
        this.category = category;
    }
}
