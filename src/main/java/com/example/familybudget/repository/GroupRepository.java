package com.example.familybudget.repository;

import com.example.familybudget.model.FamilyGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Set;

public interface GroupRepository extends JpaRepository<FamilyGroup, Long> {
        @EntityGraph(attributePaths = "members")
        @Query("SELECT g FROM FamilyGroup g JOIN g.members m WHERE m.username = :username")
        Set<FamilyGroup> findAllByMemberUsername(@Param("username") String username);

}
