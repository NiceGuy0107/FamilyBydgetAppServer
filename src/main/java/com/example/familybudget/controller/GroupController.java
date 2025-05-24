package com.example.familybudget.controller;

import com.example.familybudget.dto.*;
import com.example.familybudget.model.FamilyGroup;
import com.example.familybudget.model.Transaction;
import com.example.familybudget.model.TransactionType;
import com.example.familybudget.model.User;
import com.example.familybudget.service.GroupService;
import com.example.familybudget.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.familybudget.repository.UserRepository;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class GroupController {

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    private final GroupService groupService;
    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public GroupController(GroupService groupService,
                           TransactionService transactionService,
                           UserRepository userRepository) {
        this.groupService = groupService;
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<List<FamilyGroupDto>> getGroupsByUserId(@RequestParam Long userid) {
        logger.info("Запрос на получение групп для пользователя с ID: {}", userid);

        Optional<User> userOptional = userRepository.findById(userid);
        if (userOptional.isEmpty()) {
            logger.warn("Пользователь с ID {} не найден.", userid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String username = userOptional.get().getUsername();
        logger.info("Найден username '{}' для userId {}", username, userid);

        Set<FamilyGroup> groups = groupService.getGroupsByUsername(username);

        List<FamilyGroupDto> dtoList = groups.stream()
                .map(FamilyGroupDto::new)
                .collect(Collectors.toList());

        if (!dtoList.isEmpty()) {
            logger.info("Найдено {} групп для пользователя {}.", dtoList.size(), username);
            return ResponseEntity.ok(dtoList);
        } else {
            logger.warn("Группы для пользователя {} не найдены.", username);
            return ResponseEntity.ok(Collections.emptyList()); // <-- вот тут исправлено
        }
    }


    @PostMapping("/create")
    public ResponseEntity<FamilyGroupDto> createGroup(@RequestBody CreateGroupRequest request) {
        logger.info("Запрос на создание группы с именем: {}", request.getName());
        try {
            FamilyGroup newGroup = groupService.createGroup(request.getName(), request.getUsername());
            logger.info("Группа с именем {} успешно создана.", request.getName());
            return ResponseEntity.ok(new FamilyGroupDto(newGroup));
        } catch (IllegalStateException e) {
            logger.warn("Ошибка при создании группы: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leaveGroup(@RequestParam Long userId) {
        logger.info("Запрос на выход из группы для пользователя с ID: {}", userId);
        try {
            groupService.leaveGroup(userId);
            logger.info("Пользователь с ID {} успешно покинул группу.", userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Ошибка при выходе из группы: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/join")
    public ResponseEntity<FamilyGroupDto> joinGroup(@RequestBody JoinGroupRequest request) {
        Long groupId = request.getGroupId();
        Long userId = request.getUserId();
        logger.info("Запрос на присоединение пользователя {} к группе с ID: {}", request.getUserId(), request.getGroupId());
        try {
            FamilyGroup group = groupService.joinGroup(request.getGroupId(), request.getUserId());
            logger.info("Пользователь {} успешно присоединился к группе с ID: {}", request.getUserId(), request.getGroupId());
            return ResponseEntity.ok(new FamilyGroupDto(group));
        } catch (NoSuchElementException e) {
            logger.warn("Ошибка: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Ошибка при присоединении к группе: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    @PostMapping("/add-transaction")
    public ResponseEntity<?> addTransaction(@RequestBody AddTransactionRequest request) {
        logger.info("Received add transaction request with data: groupId={}, amount={}, username={}, type={}, dateTime={}",
                request.getGroupId(), request.getAmount(), request.getUsername(), request.getType(), request.getDateTime());

        try {
            Transaction transaction = groupService.addTransaction(
                    request.getGroupId(),
                    request.getAmount(),
                    request.getUsername(),
                    TransactionType.valueOf(request.getType()),
                    request.getDateTime()
            );
            logger.info("Transaction successfully added with ID: {}, date: {}", transaction.getId(), transaction.getDate());
            return ResponseEntity.ok(new AddTransactionRequest(transaction));
        } catch (Exception e) {
            logger.error("Error adding transaction. Request data: " + request, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding transaction: " + e.getMessage());
        }
    }
}





