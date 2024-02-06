package com.umc.lifesharing.notice.converter;

import com.umc.lifesharing.notice.dto.NoticeRequest;
import com.umc.lifesharing.notice.dto.NoticeResponse;
import com.umc.lifesharing.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class NoticeConverter {

    public static Notice toNotice(NoticeRequest.NoticeDTO noticeDTO) {
        return Notice.builder()
                .body(noticeDTO.getBody())
                .title(noticeDTO.getTitle())
                .build();
    }

    public static NoticeResponse.CreateSuccessDTO toCreateSuccessDTO(Notice notice) {
        return NoticeResponse.CreateSuccessDTO.builder()
                .noticeId(notice.getId())
                .body(notice.getBody())
                .title(notice.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static NoticeResponse.UpdateSuccessDTO toUpdateSuccessDTO(Notice notice) {
        return NoticeResponse.UpdateSuccessDTO.builder()
                .noticeId(notice.getId())
                .body(notice.getBody())
                .title(notice.getTitle())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static NoticeResponse.NoticePreviewDTO toNoticePreviewDTO(Slice<Notice> noticeSlice) {
        List<NoticeResponse.NoticeDTO> noticeList = noticeSlice.stream()
//                .sorted(Comparator.comparing(Notice::getCreatedAt).reversed())
                .map(NoticeConverter::toNoticeDTO)
                .toList();

        return NoticeResponse.NoticePreviewDTO.builder()
                .noticeList(noticeList)
                .hasNext(noticeSlice.hasNext())
                .build();
    }

    public static NoticeResponse.NoticeDTO toNoticeDTO(Notice notice) {
        return NoticeResponse.NoticeDTO.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .body(notice.getBody())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
