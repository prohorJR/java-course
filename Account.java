import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Account {
    private String id;
    private String pin;
    private double balance;
    private List<Operation> operations;
    
    private String validperiod;
    private String cvv;
    private String cardname;


    public Account(String cardname, String pin, double firstBalance) {
        this.id = generateCardNumber();
        this.pin = pin;
        this.balance = firstBalance;
        this.cardname = cardname.toUpperCase();
        this.operations = new ArrayList<Operation>();
        this.validperiod = generateValidPeriod();
        this.cvv = generateCVV();
    }

    public String getId() {
        return id;
    }

    public boolean checkPin(String inputPin) { 
        if (inputPin == null) {
            return false;
        }
        return this.pin.equals(inputPin);
    }

    public double getBalance() { 
        return balance;
    }

    public void deposit(double sum) { 
        if (sum > 0) { 
            balance += sum;
            operations.add(new Operation(OperationType.DEPOSIT, sum));
        }
    }

    public boolean withdraw(double sum) {
        if (sum > 0 && balance >= sum) {
            balance -= sum;
            operations.add(new Operation(OperationType.WITHDRAWAL, sum));
            return true;
        }
        return false;
    }

    public List<Operation> getOperations() {
        return new ArrayList<Operation>(operations);
    }

    public void showHistory() { 
        System.out.println("История транзакций");
        if (operations.isEmpty()) { 
            System.out.println("Транзакции отсутствуют");
        }
        else { 
            for (int  i = 0; i < operations.size(); i++) { 
                Operation operation = operations.get(i);
                System.out.println(operation);
            }
        }
        
    }

    public String getCardNumber() { 
        return id;
    }

    public String getValidperiod() { 
        return validperiod;
    }

    public String getCVV() {
        return cvv;
    }

    public String getcardname() { 
        return cardname; 
    }

    
}
