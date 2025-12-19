package com.example.java.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== ИНИЦИАЛИЗАЦИЯ БАЗЫ ДАННЫХ ===");
        
        // Создаем таблицу аккаунтов
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS accounts (
                id VARCHAR(50) PRIMARY KEY,
                pin VARCHAR(10),
                balance DOUBLE,
                card_name VARCHAR(100),
                valid_period VARCHAR(10),
                cvv VARCHAR(10)
            )
        """);
        System.out.println("✓ Таблица 'accounts' создана");
        
        // Создаем таблицу операций
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS operations (
                id INT AUTO_INCREMENT PRIMARY KEY,
                account_id VARCHAR(50),
                operation_date TIMESTAMP,
                type VARCHAR(20),
                amount DOUBLE,
                description VARCHAR(500)
            )
        """);
        System.out.println("✓ Таблица 'operations' создана");
        
        // Проверяем что таблицы созданы
        Integer accountCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM accounts", Integer.class);
        Integer operationCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM operations", Integer.class);
        
        System.out.println("Аккаунтов в базе: " + accountCount);
        System.out.println("Операций в базе: " + operationCount);
        
        System.out.println("\n=== H2 CONSOLE ===");
        System.out.println("URL: http://localhost:8080/h2-console");
        System.out.println("JDBC URL: jdbc:h2:mem:bankdb");
        System.out.println("User: sa");
        System.out.println("Password: (оставьте пустым)");
        System.out.println("============================\n");
    }
}