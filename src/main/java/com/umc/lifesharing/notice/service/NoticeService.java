package com.umc.lifesharing.notice.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import com.umc.lifesharing.notice.converter.NoticeConverter;
import com.umc.lifesharing.notice.dto.NoticeRequest;
import com.umc.lifesharing.notice.dto.NoticeResponse;
import com.umc.lifesharing.notice.entity.Notice;
import com.umc.lifesharing.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeResponse.NoticePreviewDTO getNoticePreviewList(Long lastNoticeId, Integer size) {
        Slice<Notice> noticeSlice = noticeRepository.findSliceByIdLessThan(lastNoticeId, PageRequest.of(0, size, Sort.by("updatedAt").descending()));
        return NoticeConverter.toNoticePreviewDTO(noticeSlice);
    }

    public NoticeResponse.CreateSuccessDTO createNotice(NoticeRequest.NoticeDTO noticeDTO) {
        Notice notice = NoticeConverter.toNotice(noticeDTO);
        notice = noticeRepository.save(notice);
        return NoticeConverter.toCreateSuccessDTO(notice);
    }

    public NoticeResponse.UpdateSuccessDTO updateNotice(Long noticeId, NoticeRequest.NoticeDTO noticeDTO) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new GeneralException(ErrorStatus.NOTICE_NOT_FOUND));
        notice.updateNotice(noticeDTO);
        noticeRepository.save(notice);
        return NoticeConverter.toUpdateSuccessDTO(notice);
    }

    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

}
