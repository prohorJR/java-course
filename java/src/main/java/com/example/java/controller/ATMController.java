package com.example.java.controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.java.models.Account;
import com.example.java.models.Operation;
import com.example.java.models.OperationType;
import com.example.java.service.BankService;
import com.example.java.service.DatabaseService;

@Component
public class ATMController implements CommandLineRunner {

    private final BankService bank;
    
    @Autowired
    private DatabaseService databaseService;
    
    private Scanner scanner;
    private Account currentAccount;

    public ATMController(BankService bankService) {
        this.bank = bankService;
    }

    @Override
    public void run(String... args) {
        this.scanner = new Scanner(System.in);
        
        // Проверка БД
        checkDatabase();
        
        startMain();
    }
    
    private void checkDatabase() {
        System.out.println("\n=== ПРОВЕРКА БАЗЫ ДАННЫХ ===");
        try {
            int count = databaseService.getAllAccounts().size();
            System.out.println("✓ База данных активна");
            System.out.println("✓ Аккаунтов в БД: " + count);
            System.out.println("✓ H2 Console: http://localhost:8080/h2-console");
        } catch (Exception e) {
            System.out.println("✗ Ошибка БД: " + e.getMessage());
        }
        System.out.println("============================\n");
    }

    public void startMain() {
        showMainMenu();
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\n=== БАНКОМАТ ===");
            System.out.println("1. Создать новую карту");
            System.out.println("2. Войти в карту");
            System.out.println("3. Выйти");
            System.out.print("Выберите: ");
            
            String choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                createNewCard();
                loginAfterCreate();
            } else if (choice.equals("2")) {
                loginToCard();
            } else if (choice.equals("3")) {
                System.out.println("До свидания!");
                return;
            } else {
                System.out.println("Неверный выбор!");
            }
        }
    }

    private void loginAfterCreate() {
        System.out.println("\nБанкомат");
        System.out.println("Вставьте карту (нажмите Enter)");
        scanner.nextLine();
        
        int attempt = 0;
        while (attempt < 3) { 
            System.out.println("Введите PIN-код для входа:");
            String entPin = scanner.nextLine();
            if (currentAccount.checkPin(entPin)) { 
                System.out.println("PIN-код введён верно. Добро пожаловать!");
                showMenu();
                return;
            } else { 
                attempt++;
                System.out.println("Неверный PIN-код! Осталось попыток: " + (3 - attempt));
            }
        }
        System.out.println("Превышено кол-во попыток!");
    }

    private void loginToCard() {
        System.out.print("\nВведите PIN карты: ");
        String pin = scanner.nextLine();
        
        Account found = bank.findAccountByPin(pin);
        if (found != null) {
            currentAccount = found;
            System.out.println("PIN-код введён верно. Добро пожаловать!");
            showMenu();
        } else {
            System.out.println("Карта не найдена!");
        }
    }

    private void createNewCard() {
        System.out.println("\nСоздание банковской карты");
        
        String name;
        while (true) {
            System.out.print("Введите ваше Имя и Фамилию(EN): ");
            name = scanner.nextLine().trim();
            if (name.matches("^[a-zA-Z]+ [a-zA-Z]+$")) {
                break;
            } else {
                System.out.println("Ошибка! Нужно Имя и Фамилия (EN)");
            }
        }

        String pin;
        while (true) {
            System.out.print("Придумайте PIN-код (ровно 4 цифры): ");
            pin = scanner.nextLine().trim();
            if (pin.matches("^\\d{4}$")) {
                break;
            } else {
                System.out.println("Ошибка! PIN должен состоять ровно из 4 цифр.");
            }
        }

        double firstBalance = 0;
        while (true) {
            System.out.print("Начальный баланс: ");
            try {
                firstBalance = Double.parseDouble(scanner.nextLine());
                if (firstBalance < 0) {
                    System.out.println("Баланс не может быть отрицательным.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите число.");
            }
        }

        this.currentAccount = bank.createAccount(name, pin, firstBalance);
        
        // СОХРАНЕНИЕ В БД
        try {
            databaseService.saveAccount(currentAccount);
            System.out.println("\n✓ Карта сохранена в базу данных!");
        } catch (Exception e) {
            System.out.println("\n✗ Ошибка сохранения в БД: " + e.getMessage());
        }
        
        System.out.println("\nКарта успешно создана!");
        currentAccount.CardInfo();
        
        // Показать сколько аккаунтов теперь в БД
        int count = databaseService.getAllAccounts().size();
        System.out.println("Всего аккаунтов в БД: " + count);
    }

    private void showMenu() {
        while (true) {
            System.out.println("\nМеню операций:");
            System.out.println("1. Баланс | 2. Снять | 3. Пополнить | 4. Пополнить мобильный | 5. История | 6. Инфо | 7. Выход");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showBalance();
                case "2" -> withdraw();
                case "3" -> deposit();
                case "4" -> refill();
                case "5" -> currentAccount.showHistory();
                case "6" -> currentAccount.CardInfo();
                case "7" -> {
                    System.out.println("Заберите карту. До свидания!");
                    return;
                }
                default -> System.out.println("Неверный ввод.");
            }
        }
    }
    
    private void showBalance() {
        System.out.println("Баланс: " + currentAccount.getBalance() + " руб.");
    }

    private void deposit() {
        while (true) {
            System.out.print("Сумма пополнения: ");
            try {
                double sum = Double.parseDouble(scanner.nextLine());
                if (sum <= 0) {
                    System.out.println("Сумма должна быть положительной!");
                    continue;
                }
                
                currentAccount.deposit(sum);
                
                // СОХРАНЕНИЕ В БД
                try {
                    databaseService.updateBalance(currentAccount.getId(), currentAccount.getBalance());
                    
                    // Создаем и сохраняем операцию
                    Operation operation = new Operation(OperationType.DEPOSIT, sum);
                    databaseService.saveOperation(operation, currentAccount.getId());
                    
                    System.out.println("✓ Операция сохранена в БД");
                } catch (Exception e) {
                    System.out.println("✗ Ошибка сохранения в БД: " + e.getMessage());
                }
                
                System.out.println("Счет пополнен на " + sum + " руб.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private void withdraw() {
        while (true) {
            System.out.print("Сумма снятия: ");
            try {
                double sum = Double.parseDouble(scanner.nextLine());
                if (sum <= 0) {
                    System.out.println("Сумма должна быть положительной!");
                    continue;
                }
                
                if (currentAccount.withdraw(sum)) {
                    // СОХРАНЕНИЕ В БД
                    try {
                        databaseService.updateBalance(currentAccount.getId(), currentAccount.getBalance());
                        
                        // Создаем и сохраняем операцию
                        Operation operation = new Operation(OperationType.WITHDRAWAL, sum);
                        databaseService.saveOperation(operation, currentAccount.getId());
                        
                        System.out.println("✓ Операция сохранена в БД");
                    } catch (Exception e) {
                        System.out.println("✗ Ошибка сохранения в БД: " + e.getMessage());
                    }
                    
                    System.out.println("Успешно! Снято: " + sum + " руб.");
                } else {
                    System.out.println("Ошибка: недостаточно средств.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }

    private void refill() {
        String phone;
        while (true) { 
            System.out.print("Номер телефона (11 цифр): ");
            phone = scanner.nextLine().trim();
            if (phone.matches("^[78]\\d{10}$")) break;
            System.out.println("Неверный формат.");
        }
                
        while (true) {
            System.out.print("Сумма пополнения: ");
            try {
                double sum = Double.parseDouble(scanner.nextLine());
                if (sum <= 0) {
                    System.out.println("Сумма должна быть положительной!");
                    continue;
                }
                
                if (currentAccount.withdraw(sum)) {
                    // СОХРАНЕНИЕ В БД
                    try {
                        databaseService.updateBalance(currentAccount.getId(), currentAccount.getBalance());
                        
                        // Создаем и сохраняем операцию .
                        Operation operation = new Operation(OperationType.REFILL, sum);
                        databaseService.saveOperation(operation, currentAccount.getId());
                        
                        System.out.println("✓ Операция сохранена в БД");
                    } catch (Exception e) {
                        System.out.println("✗ Ошибка сохранения в БД: " + e.getMessage());
                    }
                    
                    System.out.println("Телефон " + phone + " пополнен на " + sum + " руб.");
                } else {
                    System.out.println("Ошибка: недостаточно средств.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введите число!");
            }
        }
    }
}