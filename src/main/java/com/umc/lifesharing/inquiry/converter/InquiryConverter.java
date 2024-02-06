package com.umc.lifesharing.inquiry.converter;

import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.InquiryImage;
import com.umc.lifesharing.notice.entity.Notice;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InquiryConverter {

    public static Inquiry toInquiry (InquiryRequestDTO.InquiryDTO inquiryDTO) {
        return Inquiry.builder()
                .title(inquiryDTO.getTitle())
                .body(inquiryDTO.getBody())
                .build();
    }

    public static InquiryResponseDTO.InquiryDTO toInquiryResponseDTO (Inquiry inquiry) {
        return InquiryResponseDTO.InquiryDTO.builder()
                .inquiryId(inquiry.getId())
                .title(inquiry.getTitle())
                .body(inquiry.getBody())
                .imageUrlList(inquiry.getInquiryImageList())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    public static InquiryImage toInquiryImage (String imageUrl, Inquiry inquiry) {
        return InquiryImage.builder()
                .imageUrl(imageUrl)
                .inquiry(inquiry)
                .build();
    }

    public static List<InquiryImage> toInquiryImageList (List<String> imageUrlList, Inquiry inquiry) {
        return imageUrlList.stream()
                .map((image) -> toInquiryImage(image, inquiry))
                .collect(Collectors.toList());
    }

    public static InquiryResponseDTO.InquiryPreviewDTO toInquiryPreviewDTO (Slice<Inquiry> inquirySlice)  {
        List<InquiryResponseDTO.InquiryDTO> inquiryDTOList = inquirySlice.stream()
                .map(InquiryConverter::toInquiryResponseDTO)
                .collect(Collectors.toList());

        return InquiryResponseDTO.InquiryPreviewDTO.builder()
                .inquiryList(inquiryDTOList)
                .size(inquirySlice.getSize())
                .hasNext(inquirySlice.hasNext())
                .build();

    }
}
