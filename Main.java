import java.util.Scanner;

public class Main {
    private static Bank bank;
    private static Scanner scanner;
    private static Account currentAccount;
    
    public static void main(String[] args) {
        bank = new Bank();
        scanner = new Scanner(System.in);
        currentAccount = null;
        
        startMain();
    }

    public static void startMain() {
        createNewCard();
        System.out.println("Банкомат");
        System.out.println("Вставьте карту (нажмите Enter)");
        scanner.nextLine();
        int attempt = 0;
        while (attempt < 3) { 
            System.out.println("Введите PIN-код для входа:");
            String entPin = scanner.nextLine();
            if (currentAccount.checkPin(entPin)) { 
                System.out.println("PIN-код ввёден верно. Добро пожаловать!");
                showMenu();
                return;
            } else { 
                attempt++;
                System.out.println("Неверный PIN-код! Осталось попыток: " + (3 - attempt));
            }
        }
        System.out.println("Превышено кол-во попыток! Создайте новую карту.");
        createNewCard();
        startMain();
    }


private static void createNewCard() {
    System.out.println("\nСоздание банковской карты");
    String name;
    while (true) {
        System.out.print("Введите ваше имя и фамилию: ");
        name = scanner.nextLine();
        
        if (name.matches("^[a-zA-Z ]+$")) {
            break;
        } else {
            System.out.println("Ошибка! Используйте только английские буквы и пробелы.");
        }
    }
    
        String pin;
    while (true) {
        System.out.print("Придумайте PIN-код (4 цифры): ");
        pin = scanner.nextLine();
        if (pin.matches("^[0-9]{4}$")) { 
            break;
        } else { 
            System.out.println("Ошибка! В PIN-коде должно быть ровно 4 цифры");
        }
    }

    double balance = 0;
    while (true) {
        System.out.print("Внесите начальную сумму: ");
        try {
            balance = Double.parseDouble(scanner.nextLine());
            if (balance >= 0) {
                break;
            } else {
                System.out.println("Ошибка! Сумма не может быть отрицательной.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Введите корректную сумму.");
        }
    }
    
    currentAccount = new Account(name, pin, balance);
    bank.createAccount(name, pin, balance);
    
    System.out.println("\nКарта создана успешно!");
    currentAccount.CardInfo();
    
    System.out.println("\nДля продолжения нажмите любую кнопку");
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
        String phone;
        while (true) { 
            System.out.print("Введите номер телефона: ");
            phone = scanner.nextLine();
            
            if (phone.matches("^[78][0-9]{10}$")) {
                break;
            } else {
                System.out.println("Ошибка! Номер должен начинаться с 7 или 8 и содержать 11 цифр.");
            }
        }
                
        System.out.print("Сумма пополнения: ");
        try {
            double sum = Double.parseDouble(scanner.nextLine());
            if (currentAccount.withdraw(sum)) {
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