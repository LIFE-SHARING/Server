package com.umc.lifesharing.location.dto;

import com.umc.lifesharing.location.entity.Location;
import com.umc.lifesharing.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class LocationDTO {
    private String latitude;    // 위도
    private String longitude;   // 경도
    private String city;    // 시/도
    private String local;   // 군/구
    private String area;    // 읍/면/동
    private String status;

    public LocationDTO(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.city = location.getCity();
        this.local = location.getLocal();
        this.area = location.getArea();
        this.status = location.getStatus();
    }
}
