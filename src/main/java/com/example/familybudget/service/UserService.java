package com.example.familybudget.service;

import com.example.familybudget.model.User;
import com.example.familybudget.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Transactional
    public Optional<User> login(String username, String password) {
        Optional<User> optionalUser = repository.findByUsernameWithGroups(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean deleteUserByUsername(String username) {
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isPresent()) {
            repository.delete(optionalUser.get());
            return true;
        } else {
            return false;
        }
    }
}

