package com.example.java.service;

import com.example.java.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BankDatabaseService {
    
    @Autowired
    private DatabaseService databaseService;
    
    // Загрузить аккаунт из БД
    public Account loadAccountFromDatabase(String cardNumber) {
        return databaseService.loadAccount(cardNumber);
    }
    
    // Сохранить аккаунт в БД
    public void saveAccountToDatabase(Account account) {
        databaseService.saveAccount(account);
    }
    
    // Проверить PIN через БД
    public boolean validatePinFromDatabase(String cardNumber, String pin) {
        return databaseService.validatePin(cardNumber, pin);
    }
    
    // Обновить баланс в БД
    public void updateBalanceInDatabase(String cardNumber, double newBalance) {
        databaseService.updateBalance(cardNumber, newBalance);
    }
    
    // Получить все аккаунты
    public List<Account> getAllAccounts() {
        return databaseService.getAllAccounts();
    }
}