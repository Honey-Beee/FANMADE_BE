package com.unithon.domain.user.domain.entity;

import com.unithon.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String hashedPassword) {
        this.password = hashedPassword;
    }
}
