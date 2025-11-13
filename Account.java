import java.util.ArrayList;
import java.util.List;

public class Account {
    private String id;
    private String pin;
    private double balance;
    private List<Operation> operations;
    
    public Account(String id, String pin, double firstBalance) {
        this.id = id;
        this.pin = pin;
        this.balance = firstBalance;
        this.operations = new ArrayList<Operation>();
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
}
