package com.umc.lifesharing.chat.entity;

import com.umc.lifesharing.chat.entity.common.BaseEntity;
import com.umc.lifesharing.product.entity.Product;
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
public class ChatRoom  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "sender_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;

    @JoinColumn(name = "receiver_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String lastMessage;

    public void deleteSender(){
        this.sender = null;
    }

    public void deleteReceiver(){
        this.receiver = null;
    }

    public void setLastMessage(String lastMessage){
        this.lastMessage = lastMessage;
    }
}
