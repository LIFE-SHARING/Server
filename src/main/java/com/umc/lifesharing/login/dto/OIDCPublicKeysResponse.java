package com.umc.lifesharing.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublicKeysResponse {
    private List<OIDCPublicKeyDto> keys = new ArrayList<>();
}
