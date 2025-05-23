package com.example.familybudget.controller;

import com.example.familybudget.dto.AuthRequest;
import com.example.familybudget.dto.AuthResponse;
import com.example.familybudget.dto.FamilyGroupDto;
import com.example.familybudget.model.User;
import com.example.familybudget.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        User savedUser = service.register(user); // сохраняем и получаем user с ID

        List<FamilyGroupDto> groupDtos = null;
        if (savedUser.getGroups() != null && !savedUser.getGroups().isEmpty()) {
            groupDtos = savedUser.getGroups().stream()
                    .map(FamilyGroupDto::new)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(new AuthResponse(savedUser.getId(), savedUser.getUsername(), groupDtos));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> optionalUser = service.login(request.getUsername(), request.getPassword());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<FamilyGroupDto> groupDtos = null;
            if (user.getGroups() != null && !user.getGroups().isEmpty()) {
                groupDtos = user.getGroups().stream()
                        .map(FamilyGroupDto::new)
                        .collect(Collectors.toList());
            }

            AuthResponse response = new AuthResponse(user.getId(), user.getUsername(), groupDtos);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        boolean deleted = service.deleteUserByUsername(username);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}


