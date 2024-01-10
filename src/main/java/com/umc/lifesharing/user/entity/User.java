package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.review.entity.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String phone;

    private String name;

    private String profileUrl;

    private Long point;

    // Product와 연관 관계 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

}
