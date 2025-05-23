package com.example.familybudget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime date;
    private String type;
    private Long userId;
    private String username;

    public TransactionDTO(Long id, BigDecimal amount, String description, LocalDateTime date, String type, Long userId, String username) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.userId = userId;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
}
