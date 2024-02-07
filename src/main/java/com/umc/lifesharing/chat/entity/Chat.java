package com.umc.lifesharing.chat.entity;


import com.umc.lifesharing.chat.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // sql 호출을 줄이기 위해 id만 저장
    private Long roomId;

    // sql 호출을 줄이기 위해 id만 저장
    private Long sender;

    private String message;


}
