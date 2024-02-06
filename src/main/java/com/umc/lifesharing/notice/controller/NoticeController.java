package com.umc.lifesharing.notice.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.notice.dto.NoticeRequest;
import com.umc.lifesharing.notice.dto.NoticeResponse;
import com.umc.lifesharing.notice.service.NoticeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/notice")
    public ApiResponse<NoticeResponse.NoticePreviewDTO> getNoticePreviewList(@Positive @RequestParam(name = "lastNoticeId", defaultValue = "9223372036854775807") Long lastNoticeId,
                                                                             @Positive @RequestParam(name = "size") Integer size){

        return ApiResponse.onSuccess(noticeService.getNoticePreviewList(lastNoticeId, size));
    }

    @PostMapping("/admin/notice")
    public ApiResponse<NoticeResponse.CreateSuccessDTO> createNotice(@RequestBody NoticeRequest.NoticeDTO noticeDTO) {
        return ApiResponse.onSuccess(noticeService.createNotice(noticeDTO));
    }

    @PatchMapping("/admin/notice/{notice-id}")
    public ApiResponse<NoticeResponse.UpdateSuccessDTO> updateNotice(@Positive @PathVariable(name = "notice-id") Long noticeId,
                                                                     @RequestBody NoticeRequest.NoticeDTO noticeDTO) {
        return ApiResponse.onSuccess(noticeService.updateNotice(noticeId, noticeDTO));
    }

    @DeleteMapping("/admin/notice/{notice-id}")
    public ApiResponse<String> deleteNotice(@Positive @PathVariable(name = "notice-id") Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ApiResponse.onSuccess(noticeId + " 삭제 성공");
    }
}