package com.example.familybudget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class DatabaseCheckController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseCheckController(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @GetMapping("/check-database")
    public String checkDatabaseConnection() {
        try {
            // Простая проверка подключения к базе данных
            jdbcTemplate.execute("SELECT 1");
            return "Database connection is valid!";
        } catch (Exception e) {
            return "Database connection is invalid: " + e.getMessage();
        }
    }
}