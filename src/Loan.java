import java.util.Date;

public class Loan {

    String barcode;
    String userId;
    Date issueDate;
    Date dueDate;
    int NOR;
    String UserEmail;



    public Loan(String barcode, String userId ,Date issueDate, Date dueDate, int numberOfRenews) {
        this.barcode = barcode;
        this.userId = userId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.NOR = numberOfRenews;

    }

}
