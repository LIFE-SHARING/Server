package com.umc.lifesharing.inquiry.repository;


import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    Page<Inquiry> findAllByUser(User user, PageRequest pageRequest);
    void deleteByIdAndUser(Long id, User user);
}
