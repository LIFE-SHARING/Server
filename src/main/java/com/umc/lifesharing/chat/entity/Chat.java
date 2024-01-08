package com.umc.lifesharing.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    public ChatRoom chatroom_id;

    // 변경 예정 테스트
    @Column(nullable = false, length = 20)
    private Long user_id;

    @Column(nullable = false, length = 200)
    private String message;
}
