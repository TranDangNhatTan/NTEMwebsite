package com.example.testSpringBoot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne
    @MapsId // Đảm bảo ánh xạ user_id với khóa chính của User
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    public enum RoleType {
        ADMIN,
        STUDENT
    }

    // Getters và setters (được tạo tự động bởi @Data)
}