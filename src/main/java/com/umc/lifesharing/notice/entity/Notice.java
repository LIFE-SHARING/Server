package com.umc.lifesharing.notice.entity;

import com.umc.lifesharing.notice.dto.NoticeRequest;
import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "notice_id")
    private Long id;

    private String title;

    private String body;

    public Notice updateNotice(NoticeRequest.NoticeDTO noticeDTO) {
        this.title = noticeDTO.getTitle();
        this.body = noticeDTO.getBody();
        return this;
    }
}
