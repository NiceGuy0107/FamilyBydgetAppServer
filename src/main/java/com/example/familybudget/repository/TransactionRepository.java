package com.example.familybudget.repository;

import com.example.familybudget.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByGroupId(Long groupId);
    List<Transaction> findByUserId(Long userId);
}
