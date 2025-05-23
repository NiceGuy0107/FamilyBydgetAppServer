package com.example.familybudget.repository;

import com.example.familybudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("""
    SELECT DISTINCT u FROM User u
    LEFT JOIN FETCH u.groups g
    LEFT JOIN FETCH g.members
    WHERE u.username = :username
""")
    Optional<User> findByUsernameWithGroups(@Param("username") String username);

}
