package com.umc.lifesharing.notice.repository;

import com.umc.lifesharing.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Slice<Notice> findSliceByIdLessThan(Long id, Pageable pageable);
}