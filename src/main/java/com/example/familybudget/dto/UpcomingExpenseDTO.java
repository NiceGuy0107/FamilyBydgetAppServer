package com.example.familybudget.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpcomingExpenseDTO {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("amount")
    private double amount;
    
    @JsonProperty("dueDate")
    private String dueDate;
    
    @JsonProperty("groupId")
    private Long groupId;
    
    @JsonProperty("createdByUsername")
    private String createdByUsername;

    public UpcomingExpenseDTO() {}

    public UpcomingExpenseDTO(Long id, String description, double amount, LocalDateTime dueDate, Long groupId, String createdByUsername) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate != null ? dueDate.format(DateTimeFormatter.ISO_DATE_TIME) : null;
        this.groupId = groupId;
        this.createdByUsername = createdByUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getDueDateAsDateTime() {
        return dueDate != null ? LocalDateTime.parse(dueDate, DateTimeFormatter.ISO_DATE_TIME) : null;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
} 