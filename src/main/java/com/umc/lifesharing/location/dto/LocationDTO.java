package com.umc.lifesharing.location.dto;

import lombok.*;


public class LocationDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDTO {
        private String roadAddrPart1;   // 도로명 ex. 주소 서울특별시 마포구 성암로 301
        private String emdNm;       // 읍/면/동    ex. 상암동
        private String zipNo;       // 우편번호
        private String admCd;       // 행정구역코드
        private String rnMgtSn;     // 도로명코드
        private String buldMnnm;    // 건물본번
        private String detBdNmList; // 건물명
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseDTO {
        private String roadAddress;     // 도로명 ex. 주소 서울특별시 마포구 성암로 301
        private String dong;            // 읍/면/동    ex. 상암동
        private String zipCode;         // 우편번호
        private String districtCode;    // 행정구역코드
        private String readNameCode;    // 도로명코드
        private String buildingCode;    // 건물본번
        private String buildingName;   // 건물명
    }

//    public LocationDTO(Location location) {
//        this.latitude = location.getLatitude();
//        this.longitude = location.getLongitude();
//        this.city = location.getCity();
//        this.local = location.getLocal();
//        this.area = location.getArea();
//        this.status = location.getStatus();
//    }
}
