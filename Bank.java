import java.util.ArrayList;
import java.util.List;

public class Bank { 
    private List<Account> accounts;
    
    public Bank() { 
        this.accounts = new ArrayList<Account>();
    }
    
    public void createAccount(String cardname, String pin, double firstBalance) { 
        Account newAccount = new Account(cardname, pin, firstBalance);
        accounts.add(newAccount);
    }
    
    public Account findAccount(String cardNumber) { 
        for (int i = 0; i < accounts.size(); i++) { 
            Account account = accounts.get(i);
            if (account.getCardNumber().equals(cardNumber)) {
                return account;
            }
        }
        return null;
    }
}