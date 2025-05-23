package com.example.familybudget.dto;

import com.example.familybudget.model.FamilyGroup;

import com.example.familybudget.dto.UserDTO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FamilyGroupDto {
    private final Long id;
    private final String name;
    private final String ownerUsername;
    private final List<UserDTO> members;

    public FamilyGroupDto(FamilyGroup group) {
        this.id = group.getId();
        this.name = group.getName();
        this.ownerUsername = group.getOwner() != null ? group.getOwner().getUsername() : null;

        this.members = group.getMembers().stream()
                .filter(Objects::nonNull)
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOwnerUsername() { return ownerUsername; }
    public List<UserDTO> getMembers() { return members; }
}




