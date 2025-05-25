package com.example.familybudget.controller;

import com.example.familybudget.dto.UpcomingExpenseDTO;
import com.example.familybudget.model.UpcomingExpense;
import com.example.familybudget.service.UpcomingExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses/upcoming")
@CrossOrigin
public class UpcomingExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(UpcomingExpenseController.class);
    private final UpcomingExpenseService upcomingExpenseService;

    public UpcomingExpenseController(UpcomingExpenseService upcomingExpenseService) {
        this.upcomingExpenseService = upcomingExpenseService;
    }

    @PostMapping
    public ResponseEntity<UpcomingExpenseDTO> createUpcomingExpense(@RequestBody UpcomingExpenseDTO request) {
        logger.info("Создание предстоящего расхода для группы: {}", request.getGroupId());
        try {
            UpcomingExpense expense = upcomingExpenseService.createUpcomingExpense(
                request.getDescription(),
                request.getAmount(),
                request.getDueDate(),
                request.getGroupId(),
                request.getCreatedByUsername()
            );
            
            return ResponseEntity.ok(convertToDTO(expense));
        } catch (Exception e) {
            logger.error("Ошибка при создании предстоящего расхода: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<UpcomingExpenseDTO>> getUpcomingExpensesForGroup(@PathVariable Long groupId) {
        logger.info("Получение предстоящих расходов для группы: {}", groupId);
        try {
            List<UpcomingExpense> expenses = upcomingExpenseService.getUpcomingExpensesForGroup(groupId);
            List<UpcomingExpenseDTO> dtoList = expenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            logger.error("Ошибка при получении предстоящих расходов: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteUpcomingExpense(
            @PathVariable Long expenseId,
            @RequestParam String username) {
        logger.info("Удаление предстоящего расхода: {}", expenseId);
        try {
            upcomingExpenseService.deleteUpcomingExpense(expenseId, username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Ошибка при удалении предстоящего расхода: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private UpcomingExpenseDTO convertToDTO(UpcomingExpense expense) {
        return new UpcomingExpenseDTO(
            expense.getId(),
            expense.getDescription(),
            expense.getAmount(),
            expense.getDueDate(),
            expense.getGroup().getId(),
            expense.getCreatedBy().getUsername()
        );
    }
} 