import java.util.Scanner;

public class Main {
    private static Bank bank;
    private static Scanner scanner;
    private static Account currentAccount;
    
    public static void main(String[] args) {
        bank = new Bank();
        scanner = new Scanner(System.in);
        Account account = null;
        
        startMain();
    }
    
    public static void startMain() {
        System.out.println("Банкомат");
        System.out.println("Вставьте карту (нажмите Enter)");
        scanner.nextLine();
        createNewCard();
        showMenu();
    }
    
    private static void createNewCard() {
        System.out.println("\nСоздание банковской карты");
        
        System.out.print("Введите ваше имя и фамилию: ");
        String name = scanner.nextLine();
        
        System.out.print("Придумайте PIN-код (4 цифры): ");
        String pin = scanner.nextLine();
        
        System.out.print("Внесите начальную сумму: ");
        double balance = Double.parseDouble(scanner.nextLine());
        
        account = new Account(name, pin, balance);
        bank.createAccount(name, pin, balance);
        
        System.out.println("\nКарта создана успешно!");
        currentAccount.CardInfo();
        
        System.out.println("\nНажмите Enter для продолжения");
        scanner.nextLine();
    }
    
    private static void showMenu() {
        while (true) {
            System.out.println("Банкомат");
            System.out.println("1. Показать баланс");
            System.out.println("2. Снять деньги");
            System.out.println("3. Положить деньги");
            System.out.println("4. Пополнить телефон");
            System.out.println("5. История операций");
            System.out.println("6. Информация о карте");
            System.out.println("7. Выйти");
            System.out.print("Выберите операцию: ");
            
            String inputValue = scanner.nextLine();
            
            switch (inputValue) {
                case "1":
                    showBalance();
                    break;
                case "2":
                    withdraw();
                    break;
                case "3":
                    deposit();
                    break;
                case "4":
                    refill();
                    break;
                case "5":
                    showOperationHistory();
                    break;
                case "6":
                    showCardInfo();
                    break;
                case "7":
                    System.out.println("Заберите вашу карту. До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор! Попробуйте снова.");
            }
        }
    }
    
    private static void showBalance() {
        System.out.println("Ваш баланс: " + currentAccount.getBalance() + " руб.");
    }
    
    private static void withdraw() {
        System.out.print("Введите сумму для снятия: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (currentAccount.withdraw(amount)) {
                System.out.println("Успешно! Снято: " + amount + " руб.");
                System.out.println("Остаток: " + currentAccount.getBalance() + " руб.");
            } else {
                System.out.println("Ошибка: недостаточно средств!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректную сумму!");
        }
    }
    
    private static void deposit() {
        System.out.print("Введите сумму для пополнения: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount > 0) {
                currentAccount.deposit(amount);
                System.out.println("Успешно! Получено: " + amount + " руб.");
                System.out.println("Баланс: " + currentAccount.getBalance() + " руб.");
            } else {
                System.out.println("Ошибка: сумма должна быть положительной!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректную сумму!");
        }
    }
    
    private static void refill() {
        System.out.print("Введите номер телефона: ");
        String phone = scanner.nextLine();
        System.out.print("Сумма пополнения: ");
        try {
            double sum = Double.parseDouble(scanner.nextLine());
            if (account.withdraw(sum)) {
                System.out.println("Телефон " + phone + " пополнен на " + sum + " руб.");
                System.out.println("Остаток на счете: " + currentAccount.getBalance() + " руб.");
            } else {
                System.out.println("Ошибка: недостаточно средств!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите корректную сумму!");
        }
    }
    
    private static void showOperationHistory() {
        currentAccount.showHistory();
    }
    
    private static void showCardInfo() {
        currentAccount.CardInfo();
    }
}