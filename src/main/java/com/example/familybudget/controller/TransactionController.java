package com.example.familybudget.controller;

import com.example.familybudget.dto.TransactionDTO;
import com.example.familybudget.model.Transaction;
import com.example.familybudget.service.GroupService;
import com.example.familybudget.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final GroupService groupService; // ✅ добавили поле

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TransactionController.class); // ✅ логгер

    public TransactionController(TransactionService transactionService, GroupService groupService) {
        this.transactionService = transactionService;
        this.groupService = groupService;
    }

    @GetMapping("/{groupId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable Long groupId) {
        logger.info("Запрос на получение транзакций для группы с ID: {}", groupId);

        List<Transaction> transactions = transactionService.getTransactionsByGroupId(groupId);

        List<TransactionDTO> result = transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getId(),
                        BigDecimal.valueOf(t.getAmount()),
                        t.getDescription(),
                        t.getDate(),
                        t.getType().name(),
                        t.getUser().getId(),
                        t.getUser().getUsername() // ✅ получаем имя пользователя
                ))
                .collect(Collectors.toList());

        logger.info("Найдено {} транзакций для группы с ID: {}", result.size(), groupId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{groupId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long groupId) {
        logger.info("Запрос на получение баланса для группы с ID: {}", groupId);
        Double balance = groupService.getGroupBalance(groupId);
        logger.info("Баланс для группы с ID {}: {}", groupId, balance);
        return ResponseEntity.ok(balance);
    }
    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUserId(@PathVariable Long userId) {
        logger.info("Запрос на получение транзакций для пользователя с ID: {}", userId);

        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);

        List<TransactionDTO> result = transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getId(),
                        BigDecimal.valueOf(t.getAmount()),
                        t.getDescription(),
                        t.getDate(),
                        t.getType().name(),
                        t.getUser().getId(),
                        t.getUser().getUsername()
                ))
                .collect(Collectors.toList());

        logger.info("Найдено {} транзакций для пользователя с ID: {}", result.size(), userId);
        return ResponseEntity.ok(result);
    }

}
