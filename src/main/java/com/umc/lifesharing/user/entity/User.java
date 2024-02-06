package com.umc.lifesharing.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.lifesharing.location.entity.Location;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.reservation.entity.enum_class.Status;
import com.umc.lifesharing.user.entity.common.BaseEntity;
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
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

//    @Column(nullable = false)
    private String phone;

//    @Column(unique = true, nullable = false) TODO: @Column 설정하기
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    @Builder.Default
    private List<Roles> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @JsonIgnore
    private Status status = Status.ACTIVE;

    public void updateAddPoint(Long addPoint){
        this.point = this.point + addPoint;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void setTemporaryNickname() {
        this.name = this.name + getId().toString();
    }


}
