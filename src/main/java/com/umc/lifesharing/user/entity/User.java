package com.umc.lifesharing.user.entity;

import com.umc.lifesharing.location.entity.Location;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.enum_class.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private String name;

    @Column(nullable = true)
    private String profileUrl;

    @ColumnDefault("0")
    @Builder.Default
    private Long point = 0L;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SocialType socialType = SocialType.LIFESHARING;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Location> locationList = new ArrayList<>();

    public void setPassword(String password) {
        this.password = password;
    }
}
