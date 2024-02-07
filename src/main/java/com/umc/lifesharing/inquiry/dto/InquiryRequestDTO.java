package com.umc.lifesharing.inquiry.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InquiryRequestDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InquiryDTO {
        @NotEmpty
        private String title;

        @NotEmpty
        private String body;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ReplyDTO {
        @NotEmpty
        private String body;
    }
}
