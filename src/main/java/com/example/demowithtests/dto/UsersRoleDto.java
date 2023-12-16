package com.example.demowithtests.dto;


public record UsersRoleDto (
     Long id,
     String username,
     String password,
     String role,
     String role_2){
    public UsersRoleDto(Long id,
                       String username,
                       String password,
                       String role,
                       String role_2) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = "USER";
        this.role_2 = role_2;
    }
}
