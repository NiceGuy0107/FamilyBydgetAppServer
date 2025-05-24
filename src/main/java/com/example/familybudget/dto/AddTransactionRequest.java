package com.example.familybudget.dto;

import java.time.LocalDateTime;

import com.example.familybudget.model.Transaction;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AddTransactionRequest {
    private Long id;
    private double amount;
    private String username;
    private Long groupId;
    private String type;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateTime;

    // ✅ Добавить пустой конструктор
    public AddTransactionRequest() {
    }

    // Конструктор из Transaction — полезен, но не используется при десериализации
    public AddTransactionRequest(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.username = transaction.getUser().getUsername();
        this.groupId = transaction.getGroup().getId();
        this.type = transaction.getType().name();
        this.dateTime = transaction.getDate();
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
