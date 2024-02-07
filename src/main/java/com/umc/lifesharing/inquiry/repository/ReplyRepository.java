package com.umc.lifesharing.inquiry.repository;


import com.umc.lifesharing.inquiry.entity.Inquiry;
import com.umc.lifesharing.inquiry.entity.Reply;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findByInquiry(Inquiry inquiry);
}
