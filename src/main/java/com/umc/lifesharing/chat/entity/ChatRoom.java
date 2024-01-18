package com.umc.lifesharing.chat.entity;

import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 유저로 수정 예정
    //@ManyToOne(fetch = FetchType.LAZY)
    private Long sender;

    //@ManyToOne(fetch = FetchType.LAZY)
    private Long receiver;
}
