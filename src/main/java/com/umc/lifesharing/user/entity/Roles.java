package com.umc.lifesharing.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.lifesharing.user.entity.common.BaseEntity;
import com.umc.lifesharing.user.entity.enum_class.UserRoles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Roles extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRoles userAuth = UserRoles.ROLE_USER;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Roles addUser(User user) {
        this.user = user;
        user.getRoles().add(this);
        return this;
    }

    public void toAdmin() {
        userAuth = UserRoles.ROLE_ADMIN;
    }
}