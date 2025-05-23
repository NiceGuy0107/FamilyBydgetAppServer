package com.example.familybudget.dto;

import java.util.List;

public class AuthResponse {
    private long id;
    private String username;
    private List<FamilyGroupDto> groups;

    // Конструктор
    public AuthResponse(long id, String username, List<FamilyGroupDto> groups) {
        this.id = id;
        this.username = username;
        this.groups = groups;
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) { // был int — исправил на long
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<FamilyGroupDto>  getGroups() {
        return groups;
    }

    public void setGroup(List<FamilyGroupDto> groups ) {
        this.groups = groups;
    }
}
