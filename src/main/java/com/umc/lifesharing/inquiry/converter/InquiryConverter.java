package com.umc.lifesharing.inquiry.converter;

import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.InquiryImage;
import com.umc.lifesharing.inquiry.entity.Reply;
import org.springframework.data.domain.Slice;

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
                .imageUrlList(inquiry.getInquiryImageList().stream()
                        .map(InquiryImage::getImageUrl)
                        .collect(Collectors.toList()))
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

    public static InquiryResponseDTO.InquiryAndReplyDTO toInquiryAndReplayDTO(Reply reply, Inquiry inquiry) {
        return InquiryResponseDTO.InquiryAndReplyDTO.builder()
                .inquiry(inquiry)
                .reply(reply)
                .build();
    }

}
