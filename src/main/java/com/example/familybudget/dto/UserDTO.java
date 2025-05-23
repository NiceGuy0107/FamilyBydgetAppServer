package com.example.familybudget.dto;

import com.example.familybudget.model.User;

public class UserDTO {
    private Long id;
    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
}
