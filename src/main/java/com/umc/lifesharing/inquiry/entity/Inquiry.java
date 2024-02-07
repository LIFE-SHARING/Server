package com.umc.lifesharing.inquiry.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inquiry_id")
    private Long id;

    @Builder.Default
    private String state = "대기";

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY)
    @Column(nullable = true)
    @Builder.Default
    private List<InquiryImage> inquiryImageList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "reply_id", nullable = true)
    @JsonIgnore
    private Reply reply;

    public Inquiry addUser(User user) {
        this.user = user;
        user.getInquiryList().add(this);
        return this;
    }

    public Inquiry addReply(Reply reply) {
        this.reply = reply;
        return this;
    }

    public void setInquiryImageList(List<InquiryImage> inquiryImageList) {
        this.inquiryImageList = inquiryImageList;
    }

    public void updateState() {
        this.state = "답변";
    }
}
