package com.umc.lifesharing.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TermDto {
    @NotNull
    @Positive
    private Long id;

    @NotNull
    private Boolean requried;
}
