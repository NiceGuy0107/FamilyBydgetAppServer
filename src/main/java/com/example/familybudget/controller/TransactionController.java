package com.example.familybudget.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.familybudget.dto.AddTransactionRequest;
import com.example.familybudget.dto.TransactionDTO;
import com.example.familybudget.model.Transaction;
import com.example.familybudget.model.TransactionType;
import com.example.familybudget.service.GroupService;
import com.example.familybudget.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final GroupService groupService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionService, GroupService groupService) {
        this.transactionService = transactionService;
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody AddTransactionRequest request) {
        logger.info("Запрос на добавление транзакции: группа {} сумма {} пользователь {}",
                request.getGroupId(), request.getAmount(), request.getUsername());

        try {
            Transaction transaction = groupService.addTransaction(
                    request.getGroupId(),
                    request.getAmount(),
                    request.getUsername(),
                    TransactionType.valueOf(request.getType())
            );
            logger.info("Транзакция успешно добавлена: {}", transaction);
            return ResponseEntity.ok(new TransactionDTO(
                    transaction.getId(),
                    BigDecimal.valueOf(transaction.getAmount()),
                    transaction.getDescription(),
                    transaction.getDate(),
                    transaction.getType().name(),
                    transaction.getUser().getId(),
                    transaction.getUser().getUsername()
            ));
        } catch (Exception e) {
            logger.error("Ошибка при добавлении транзакции: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при добавлении транзакции: " + e.getMessage());
        }
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
