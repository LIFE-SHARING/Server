package com.umc.lifesharing.inquiry.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.inquiry.service.InquiryService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    @PostMapping("/user/inquiry")
    public ApiResponse<InquiryResponseDTO.InquiryDTO> createInquiry(@AuthenticationPrincipal UserAdapter userAdapter,
                                                                    @RequestPart(value = "inquiryDTO") InquiryRequestDTO.InquiryDTO inquiryDTO,
                                                                    @Nullable @RequestPart(value = "multipartFiles") List<MultipartFile> multipartFiles) {
        return ApiResponse.onSuccess(inquiryService.createInquiry(userAdapter, inquiryDTO, multipartFiles));
    }

    @GetMapping("/user/inquires")
    public ApiResponse<InquiryResponseDTO.InquiryPreviewDTO> getInquiryList(@AuthenticationPrincipal UserAdapter userAdapter,
                                                                            @Positive @RequestParam(name = "lastInquiryId", defaultValue = "9223372036854775807") Long lastInquiryId,
                                                                            @Positive @RequestParam(name = "size") Integer size) {
        return ApiResponse.onSuccess(inquiryService.getInquiryList(userAdapter, lastInquiryId, size));
    }

    @DeleteMapping("/user/inquiry/{inquiry-id}")
    public ApiResponse<String> deleteInquiry(@AuthenticationPrincipal UserAdapter userAdapter,
                                             @Positive @PathVariable(name = "inquiry-id") Long inquiryId){
        return ApiResponse.onSuccess(inquiryService.deleteInquiry(userAdapter, inquiryId));
    }

    @GetMapping("/user/inquiry/{inquiry-id}")
    public ApiResponse<InquiryResponseDTO.InquiryAndReplyDTO> getInquiry(@AuthenticationPrincipal UserAdapter userAdapter,
                                                                         @Positive @PathVariable(name = "inquiry-id") Long inquiryId) {
        return ApiResponse.onSuccess(inquiryService.getInquiry(userAdapter, inquiryId));
    }

    @PostMapping("/admin/reply/inquiry/{inquiry-id}")
    public ApiResponse<InquiryResponseDTO.InquiryAndReplyDTO> createReply(@AuthenticationPrincipal UserAdapter userAdapter,
                                             @RequestBody InquiryRequestDTO.ReplyDTO replayDTO,
                                             @Positive @PathVariable(name = "inquiry-id") Long inquiryId){
        return ApiResponse.onSuccess(inquiryService.createReply(replayDTO, inquiryId));
    }
}
