import java.text.SimpleDateFormat;
import java.util.Date;

public class Operation {
    private Date date;
    private String type;
    private double sum;
    private String info;
    
    public Operation(String type, double sum, String info) {
        this.date = new Date();
        this.type = type;
        this.sum = sum;
        this.info = info;
    }
    
    public Date getDate() {
    return date; 
}

    public String getType() { 
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
}// чоко и пай