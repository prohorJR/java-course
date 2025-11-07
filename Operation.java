import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation {
    private Date date;
    private OperationType type;
    private double sum;
    private String info;
    
    public Operation(OperationType type, double sum) {
        this.date = new Date();
        this.type = type;
        this.sum = sum;
        
        if (type == OperationType.DEPOSIT) {
            this.info = "Пополнение счета через банкомат";
        } else if (type == OperationType.WITHDRAWAL) {
            this.info = "Снятие наличных в банкомате";
        } else {
            this.info = "Пополнение телефона";
        }
    }
    
    public Date getDate() {
        return date; 
    }

    public OperationType getType() { 
        return type; 
    }
    
    public double getSum() {
        return sum; 
    }

    public String getInfo() { 
        return info; 
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return "Дата: " + formattedDate + " | " + type + " | " + sum + " руб. | " + info;
    }
}