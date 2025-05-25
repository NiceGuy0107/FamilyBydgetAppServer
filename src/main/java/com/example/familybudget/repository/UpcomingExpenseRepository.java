package com.example.familybudget.repository;

import com.example.familybudget.model.UpcomingExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpcomingExpenseRepository extends JpaRepository<UpcomingExpense, Long> {
    List<UpcomingExpense> findByGroupId(Long groupId);
} 