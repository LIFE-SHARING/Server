package com.umc.lifesharing.location.entity;

import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 위치는 한 가지만 가질 수 있기 때문에 OneToOne이지만 추후 여러 값을 가질 경우를 대비하여 1:N으로 엮음
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String roadAddress;     // 도로명 ex. 주소 서울특별시 마포구 성암로 301
    private String dong;            // 읍/면/동    ex. 상암동
    private String zipCode;         // 우편번호
    private String districtCode;    // 행정구역코드
    private String readNameCode;    // 도로명코드
    private String buildingCode;    // 건물본번
    private String buildingName;    // 건물명

    public Location addUser(User user) {
        this.user = user;
        user.getLocationList().add(this);
        return this;
    }
}
