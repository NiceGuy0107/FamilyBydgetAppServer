package com.example.familybudget.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.familybudget.model.FamilyGroup;
import com.example.familybudget.model.Transaction;
import com.example.familybudget.model.TransactionType;
import com.example.familybudget.model.User;
import com.example.familybudget.repository.GroupRepository;
import com.example.familybudget.repository.TransactionRepository;
import com.example.familybudget.repository.UserRepository;


@Service
public class GroupService {

    private final GroupRepository groupRepo;
    private final UserRepository userRepo;
    private final TransactionRepository txnRepo;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GroupService.class);

    public GroupService(GroupRepository groupRepo, UserRepository userRepo, TransactionRepository txnRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
        this.txnRepo = txnRepo;
    }

    public Set<FamilyGroup> getGroupsByUsername(String username) {
        return groupRepo.findAllByMemberUsername(username);
    }

    @Transactional
    public FamilyGroup createGroup(String name, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        Set<FamilyGroup> existingGroups = groupRepo.findAllByMemberUsername(username);
        if (!existingGroups.isEmpty()) {
            throw new IllegalStateException("Пользователь уже состоит в группе");
        }

        FamilyGroup group = new FamilyGroup();
        group.setName(name);
        group.setOwner(user);
        group.getMembers().add(user);

        user.getGroups().add(group);

        FamilyGroup savedGroup = groupRepo.save(group);
        userRepo.save(user);
        return savedGroup;
    }


    @Transactional
    public void leaveGroup(Long userId) {
        // Находим пользователя по ID
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь с ID " + userId + " не найден"));

        // Получаем список групп пользователя
        Set<FamilyGroup> groups = user.getGroups();
        if (!groups.isEmpty()) {
            // Берем первую группу (можно доработать для выбора конкретной группы)
            FamilyGroup group = groups.iterator().next();

            // Удаляем пользователя из группы
            group.getMembers().remove(user);
            user.getGroups().remove(group);

            // Сохраняем изменения
            groupRepo.save(group);
            userRepo.save(user);

            // Опционально — удалить группу, если больше нет участников
            long count = group.getMembers().size(); // количество участников в группе после удаления
            if (count == 0) {
                groupRepo.delete(group);
            }
        } else {
            throw new IllegalStateException("У пользователя нет групп для выхода");
        }
    }

    @Transactional
    public FamilyGroup joinGroup(Long groupId, Long userId) {
        FamilyGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Группа не найдена"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            user.getGroups().add(group);

            userRepo.save(user);
            group = groupRepo.save(group);
        }

        return group;
    }


    public List<Transaction> getTransactionsForGroup(Long groupId) {
        return txnRepo.findByGroupId(groupId);
    }

    public double getGroupBalance(Long groupId) {
        List<Transaction> transactions = txnRepo.findByGroupId(groupId);
        return transactions.stream()
                .mapToDouble(txn -> txn.getType() == TransactionType.INCOME ? txn.getAmount() : -txn.getAmount())
                .sum();
    }

    @Transactional
    public Transaction addTransaction(Long groupId, double amount, String username, TransactionType type, LocalDateTime dateTime) {
        logger.info("Добавление транзакции: groupId={}, amount={}, username={}, type={}, dateTime={}", 
            groupId, amount, username, type, dateTime);

        FamilyGroup group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Группа не найдена"));

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Пользователь не найден"));

        if (!group.getMembers().contains(user)) {
            throw new IllegalStateException("Пользователь не является членом группы");
        }

        Transaction transaction = new Transaction();
        transaction.setGroup(group);
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setDate(dateTime != null ? dateTime : LocalDateTime.now());
        transaction.setType(type);

        logger.info("Сохранение транзакции с датой: {}", transaction.getDate());
        return txnRepo.save(transaction);
    }
}



