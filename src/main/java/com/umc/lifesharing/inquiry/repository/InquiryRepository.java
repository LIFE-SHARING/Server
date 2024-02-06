package com.umc.lifesharing.inquiry.repository;


import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Slice<Inquiry> findSliceByIdLessThanAndUser(Long Id, User user, Pageable pageable);
    void deleteByIdAndUser(Long id, User user);
}
