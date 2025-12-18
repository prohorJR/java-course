package com.example.java.controller;

import com.example.java.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class DatabaseController {
    
    @Autowired
    private DatabaseService databaseService;
    
    @GetMapping("/api/db/accounts/count")
    public int getAccountsCount() {
        List<?> accounts = databaseService.getAllAccounts();
        return accounts != null ? accounts.size() : 0;
    }
    
    @GetMapping("/api/db/status")
    public String getStatus() {
        return "База данных работает";
    }
}