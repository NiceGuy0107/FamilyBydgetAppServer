package com.example.familybudget.service;

import com.example.familybudget.model.UpcomingExpense;
import com.example.familybudget.model.FamilyGroup;
import com.example.familybudget.model.User;
import com.example.familybudget.repository.UpcomingExpenseRepository;
import com.example.familybudget.repository.GroupRepository;
import com.example.familybudget.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UpcomingExpenseService {
    private final UpcomingExpenseRepository upcomingExpenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public UpcomingExpenseService(
            UpcomingExpenseRepository upcomingExpenseRepository,
            GroupRepository groupRepository,
            UserRepository userRepository) {
        this.upcomingExpenseRepository = upcomingExpenseRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UpcomingExpense createUpcomingExpense(String description, double amount, String dueDate, Long groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        FamilyGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Группа не найдена"));

        if (!group.getMembers().contains(user)) {
            throw new IllegalStateException("Пользователь не является членом группы");
        }

        LocalDateTime parsedDueDate = LocalDateTime.parse(dueDate);

        UpcomingExpense expense = new UpcomingExpense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setDueDate(parsedDueDate);
        expense.setGroup(group);
        expense.setCreatedBy(user);

        return upcomingExpenseRepository.save(expense);
    }

    public List<UpcomingExpense> getUpcomingExpensesForGroup(Long groupId) {
        return upcomingExpenseRepository.findByGroupId(groupId);
    }

    @Transactional
    public void deleteUpcomingExpense(Long expenseId, String username) {
        UpcomingExpense expense = upcomingExpenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("Расход не найден"));

        if (!expense.getCreatedBy().getUsername().equals(username)) {
            throw new IllegalStateException("Только создатель может удалить расход");
        }

        upcomingExpenseRepository.delete(expense);
    }
} 