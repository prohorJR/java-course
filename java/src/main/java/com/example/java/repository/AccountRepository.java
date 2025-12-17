package com.example.java.repository;

import com.example.java.models.Account;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    public void save(Account account) {
        accounts.add(account);
    }

    public Optional<Account> findByCardNumber(String cardNumber) {
        return accounts.stream()
                .filter(a -> a.getCardNumber().equals(cardNumber))
                .findFirst();
    }
}