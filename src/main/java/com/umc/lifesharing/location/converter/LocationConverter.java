package com.umc.lifesharing.location.converter;

import com.umc.lifesharing.location.dto.LocationDTO;
import com.umc.lifesharing.location.entity.Location;

import java.util.List;

public class LocationConverter {
    public static LocationDTO.ResponseDTO toResponseDTO(List<Location> locations) {

        Location location = locations.get(0);
        return LocationDTO.ResponseDTO.builder()
                .roadAddress(location.getRoadAddress())
                .dong(location.getDong())
                .zipCode(location.getZipCode())
                .districtCode(location.getDistrictCode())
                .readNameCode(location.getReadNameCode())
                .buildingCode(location.getBuildingCode())
                .buildingName(location.getBuildingName())
                .build();
    }

    public static Location toLocation(LocationDTO.RequestDTO location) {
        return Location.builder()
                .roadAddress(location.getRoadAddrPart1())
                .dong(location.getEmdNm())
                .zipCode(location.getZipNo())
                .districtCode(location.getAdmCd())
                .readNameCode(location.getRnMgtSn())
                .buildingCode(location.getBuldMnnm())
                .buildingName(location.getDetBdNmList())
                .build();
    }
}
