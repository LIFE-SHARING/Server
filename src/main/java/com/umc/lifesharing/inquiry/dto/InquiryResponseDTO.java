package com.umc.lifesharing.inquiry.dto;

import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.InquiryImage;
import com.umc.lifesharing.notice.dto.NoticeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class InquiryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquiryPreviewDTO {
        private List<InquiryDTO> inquiryList;
        private Integer size;
        private Boolean hasNext;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateInquiryDTO {
        private Long inquiryId;
        private LocalDateTime createdAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InquiryDTO {
        private Long inquiryId;
        private String title;
        private String body;
        private List<InquiryImage> imageUrlList;
        private LocalDateTime createdAt;
    }
}
