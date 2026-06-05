
import com.bths.dsa.TransactionManagement;
import com.bths.entity.Transaction;


public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Test case for addLast
        TransactionManagement tm = new TransactionManagement();
        Transaction t1 = new Transaction("TXN001", "ACC001", 500000.00, "DEPOSIT", "2026-06-05 08:00");
        tm.addLast(t1);
        tm.displayTransaction();
    }

}
