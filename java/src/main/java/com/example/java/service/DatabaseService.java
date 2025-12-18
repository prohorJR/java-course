package com.example.java.service;

import com.example.java.models.Account;
import com.example.java.models.Operation;
import com.example.java.models.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Вспомогательный метод для получения PIN через рефлексию
    private String getPinFromAccount(Account account) {
        try {
            Field pinField = Account.class.getDeclaredField("pin");
            pinField.setAccessible(true);
            return (String) pinField.get(account);
        } catch (Exception e) {
            return "0000"; // значение по умолчанию
        }
    }
    
    // Вспомогательный метод для установки ID через рефлексию
    private void setIdToAccount(Account account, String id) {
        try {
            Field idField = Account.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(account, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Сохранить аккаунт в БД
    public void saveAccount(Account account) {
        String sql = "INSERT INTO accounts (id, pin, balance, card_name, valid_period, cvv) VALUES (?, ?, ?, ?, ?, ?)";
        
        jdbcTemplate.update(sql,
            account.getId(),
            getPinFromAccount(account),
            account.getBalance(),
            account.getcardname(),
            account.getValidperiod(),
            account.getCVV()
        );
        
        // Сохраняем операции
        for (Operation operation : account.getOperations()) {
            saveOperation(operation, account.getId());
        }
    }
    
    // Загрузить аккаунт из БД
    public Account loadAccount(String cardNumber) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try {
            Account account = jdbcTemplate.queryForObject(sql, new AccountRowMapper(), cardNumber);
            
            // Загружаем операции
            List<Operation> operations = getOperations(cardNumber);
            
            // Добавляем операции в аккаунт
            for (Operation operation : operations) {
                // Используем рефлексию для доступа к списку операций
                try {
                    Field operationsField = Account.class.getDeclaredField("operations");
                    operationsField.setAccessible(true);
                    List<Operation> ops = (List<Operation>) operationsField.get(account);
                    ops.add(operation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            return account;
        } catch (Exception e) {
            return null;
        }
    }
    
    // Проверить PIN
    public boolean validatePin(String cardNumber, String pin) {
        try {
            Account account = loadAccount(cardNumber);
            return account != null && account.checkPin(pin);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Обновить баланс
    public void updateBalance(String cardNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, cardNumber);
    }
    
    // Сохранить операцию
    public void saveOperation(Operation operation, String accountId) {
        String sql = "INSERT INTO operations (account_id, operation_date, type, amount, description) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            accountId,
            operation.getDate(),
            operation.getType().name(),
            operation.getSum(),
            operation.getInfo()
        );
    }
    
    // Получить историю операций
    public List<Operation> getOperations(String accountId) {
        String sql = "SELECT * FROM operations WHERE account_id = ? ORDER BY operation_date DESC";
        try {
            return jdbcTemplate.query(sql, new OperationRowMapper(), accountId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    // Получить все аккаунты
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM accounts";
        try {
            return jdbcTemplate.query(sql, new AccountRowMapper());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    // Маппер для Account (без использования сеттеров)
    private class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Создаем аккаунт через конструктор
            Account account = new Account(
                rs.getString("card_name"),
                rs.getString("pin"),
                rs.getDouble("balance")
            );
            
            // Устанавливаем ID через рефлексию
            setIdToAccount(account, rs.getString("id"));
            
            // Устанавливаем дополнительные поля через рефлексию
            try {
                Field validperiodField = Account.class.getDeclaredField("validperiod");
                validperiodField.setAccessible(true);
                validperiodField.set(account, rs.getString("valid_period"));
                
                Field cvvField = Account.class.getDeclaredField("cvv");
                cvvField.setAccessible(true);
                cvvField.set(account, rs.getString("cvv"));
                
                Field operationsField = Account.class.getDeclaredField("operations");
                operationsField.setAccessible(true);
                operationsField.set(account, new ArrayList<Operation>());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return account;
        }
    }
    
    // Маппер для Operation
    private class OperationRowMapper implements RowMapper<Operation> {
        @Override
        public Operation mapRow(ResultSet rs, int rowNum) throws SQLException {
            OperationType type = OperationType.valueOf(rs.getString("type"));
            Operation operation = new Operation(type, rs.getDouble("amount"));
            return operation;
        }
    }
}