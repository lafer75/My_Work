package com.example.demowithtests.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users_role")
public class UsersRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "username")
    private String username;


    @JoinColumn(name = "password")
    private String password;

    @JoinColumn(name = "role")
    private String role;

    @JoinColumn(name = "role_2")
    private String role_2;

}
