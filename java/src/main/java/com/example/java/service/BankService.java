package com.example.java.service;

import com.example.java.models.Account;
import com.example.java.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BankService {
    private final AccountRepository repository;

    public BankService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(String name, String pin, double balance) {
        Account newAccount = new Account(name, pin, balance);
        repository.save(newAccount);
        return newAccount;
    }

    public Account getAccount(String cardNumber) {
        return repository.findByCardNumber(cardNumber).orElse(null);
    }
    
    public Account findAccountByPin(String pin) {
        List<Account> allAccounts = repository.findAll();
        
        for (Account account : allAccounts) {
            if (account.checkPin(pin)) {
                return account;
            }
        }
        return null;
    }
}