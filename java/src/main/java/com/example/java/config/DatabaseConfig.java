package com.example.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    
    @Bean
    public DataSource dataSource() {
        org.h2.jdbcx.JdbcDataSource dataSource = new org.h2.jdbcx.JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
        // Создаем простые таблицы
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
        
        return jdbcTemplate;
    }
}