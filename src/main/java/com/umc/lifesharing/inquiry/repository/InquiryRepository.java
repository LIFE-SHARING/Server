package com.umc.lifesharing.inquiry.repository;


import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Slice<Inquiry> findSliceByIdLessThanAndUser(Long Id, User user, Pageable pageable);

    Optional<Inquiry> findByIdAndUser(Long Id, User user);
    void deleteByIdAndUser(Long id, User user);
}
