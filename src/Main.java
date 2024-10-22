import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;

public class Main {


    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final int BookLoanTime = 28; // 4 weeks, that what ill be giving then in loans
    private static final int MediaLoanTime = 7; // 1 week, prob
    private static final int renewMAXB = 3; // this means the max renews for the books
    private static final int renewMAXMed = 2;

    private Map<String, Items> items = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private List<Loan> loans = new ArrayList<>();


    private static final String StaffPass = "12345"; // This is the hardcoded staff password

    private static String itemsF = "C:\\Users\\kfxue\\IdeaProjects\\FinalJavaProj\\src\\ITEMS.csv"; //Link this to the Items file.
    private static String userF = "C:\\Users\\kfxue\\IdeaProjects\\FinalJavaProj\\src\\USERS.csv"; // link this to the User file
    private static String LF = "C:\\Users\\kfxue\\IdeaProjects\\FinalJavaProj\\src\\LOANS.csv"; // link this to the loans file




    public static void main(String[] args) {

        Main librarySys = new Main(); //Load main Class.
        librarySys.LibLoadItems();
        librarySys.LibSysloadUsers();
        librarySys.LibSysloadLoans();
        Scanner scanner = new Scanner(System.in);

        //Art Generated from https://www.ascii-art-generator.org/.
        System.out.println(" _           _                _     _ _                        _____ _               _               _   \n| |         | |              | |   (_) |                      /  __ \\ |             | |             | |  \n| |    _   _| |_ ___  _ __   | |    _| |__   __ _ _ __ _   _  | /  \\/ |__   ___  ___| | _____  _   _| |_ \n| |   | | | | __/ _ \\| '_ \\  | |   | | '_ \\ / _` | '__| | | | | |   | '_ \\ / _ \\/ __| |/ / _ \\| | | | __|\n| |___| |_| | || (_) | | | | | |___| | |_) | (_| | |  | |_| | | \\__/\\ | | |  __/ (__|   < (_) | |_| | |_ \n\\_____/\\__,_|\\__\\___/|_| |_| \\_____/_|_.__/ \\__,_|_|   \\__, |  \\____/_| |_|\\___|\\___|_|\\_\\___/ \\__,_|\\__|\n                                                        __/ |                                            \n                                                       |___/                                             ");

        System.out.println("________________________________________________________________________________");//graphics
        System.out.println("Welcome to the Luton Libary Checkout-System"); //Greet user
        //System.out.println("________________________________________________________________________________");

        System.out.println("What would you like to do? ");
        int MenuUserInput = -1;
        while (true){

            try{
                System.out.println("________________________________________________________________________________");
                System.out.println("[1] Issue an Book/Media \n[2] Renew an Book/Media \n[3] Return an Book/Media \n[4] View all [Loans] \n[5] Exit Programme \n________________________________________________________________________________ \nPlease Enter Number:  "); //Menu system:
                MenuUserInput = scanner.nextInt();
                scanner.nextLine();
            }
            catch(Exception e){
                System.out.println("Invalid Input. Needs to be a integer");
                break;

            }
            String UserInputPassword = "";


            switch (MenuUserInput){
                case -1:
                    System.out.println("________________________________________________________________________________\nInvalid Input. \nERROR: Please input a number that is on the menu. \n________________________________________________________________________________");

                    break;
                case 1:
                    System.out.println("Running: [1] Issue an Book/Media \n________________________________________________________________________________");
                    System.out.println("Please enter Staff Passcode to issue a loan: "); // the staff password is later added.
                    UserInputPassword = scanner.nextLine();
                    if (!UserInputPassword.equals(StaffPass)) {
                        System.out.println("Access denied.");
                        scanner.close();
                        break;
                    }
                    else {

                        librarySys.LibSysissueItem(scanner);

                    }

                    break;
                case 2:
                    System.out.println("Running: [2] Renew an Book/Media \n________________________________________________________________________________");

                    System.out.println("Please enter Staff Passcode to issue a loan: "); // the staff password is later added.
                    UserInputPassword = scanner.nextLine();
                    if (!UserInputPassword.equals(StaffPass)) {
                        System.out.println("Access denied.");
                        scanner.close();
                        break;
                    }
                    else {

                        librarySys.LibSysrenewItem(scanner);

                    }


                    break;
                case 3:
                    System.out.println("Running: [3] Return an Book/Media \n________________________________________________________________________________");
                    librarySys.LibSysreturnItem(scanner);
                    break;
                case 4:
                    System.out.println("Running: [4] View all [Loans] \n________________________________________________________________________________");
                    librarySys.LibSysviewLoans();
                    break;
                case 5:
                    librarySys.LibSyssaveLoans();
                    System.out.println("Exiting... \n________________________________________________________________________________\nProgramme has been terminated");
                    System.exit(0);
                    break;
                default:
                    System.out.println("________________________________________________________________________________\nInvalid Input. \nERROR: Please input a number that is on the menu. \n________________________________________________________________________________\nExiting...");
                    //System.exit(0);
                    break;


            }

        }



    }
    //
    // this allow us to display all the loans
    //
    private void LibSysviewLoans() {
        if (loans.isEmpty()) {

            System.out.println("Sorry, No current loans available.");





            return;
        }
        for (Loan loan : loans) {

            Items i = items.get(loan.barcode);
            System.out.println("Loan: Barcode = " + loan.barcode + ", User ID = " + loan.userId + ", Title = " + i.getTitle() +  ", Type = " + i.getType() +
                    ", Issue Date = " + sdf.format(loan.issueDate) +
                    ", Due Date = " + sdf.format(loan.dueDate) +
                    ", Number of Renews = " + loan.NOR);
        }
    }
    //
    // this is the procedure to issue a loan
    //
    private void LibSysissueItem(Scanner scanner) {
        System.out.print("Enter item barcode: ");
        String barcode = scanner.nextLine();
        if (!items.containsKey(barcode)) {
            System.out.println("Sorry this item does not exist. please add the item to the database.");
            return;
        }
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        if (!users.containsKey(userId)) {
            System.out.println("Sorry this user does not exist.");
            return;
        }

        Date issueDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(issueDate);
        Items item =items.get(barcode);
        int loanPeriod = items.get(barcode).type.equals("Book") ? BookLoanTime : MediaLoanTime;
        cal.add(Calendar.DAY_OF_MONTH, loanPeriod);
        Date dueDate = cal.getTime();

        Loan loan = new Loan(barcode, userId ,issueDate, dueDate, 0);
        loans.add(loan);
        System.out.println("Item has issued successfully.");
    }
    //
    // this is the procedure to save a loan (when the program is exiting)
    //

    private void LibSyssaveLoans() {
        try (BufferedWriter Bw = new BufferedWriter(new FileWriter(LF))) {


            for (Loan loan : loans) {

                Bw.write(loan.barcode + "," + loan.userId + "," + sdf.format(loan.issueDate) + "," +
                        sdf.format(loan.dueDate) + "," + loan.NOR);
                Bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //
// this is the procedure when returning a loan
    //
    private void LibSysreturnItem(Scanner scanner) {
        System.out.print("Enter item barcode to return: ");
        String barcode = scanner.nextLine();
        Loan loanToRemove = null;
        for (Loan loan : loans) {
            if (loan.barcode.equals(barcode)) {
                loanToRemove = loan;
                break;
            }
        }
        if (loanToRemove != null) {
            loans.remove(loanToRemove);
            System.out.println("the item has returned successfully.");
        } else {
            System.out.println("sorry no loan is found");
        }
    }
//
    // this is for renewing the loan (ALWAYS BREAKKKKK)
    //
    private void LibSysrenewItem(Scanner scanner) {
        System.out.print("Enter item barcode for renewal: ");
        String barcode = scanner.nextLine();
        List<Loan> filteredLoans = loans.stream()
                .filter(loan -> loan.barcode.equals(barcode))
                .collect(Collectors.toList());

        if (filteredLoans.isEmpty()) {
            System.out.println("Sorry no loan has been found for that number");
            return;
        }

        Loan loan = filteredLoans.get(0); // Assuming one loan per item for simplicity
        if ((loan.NOR >= renewMAXB && items.get(barcode).type.equals("Book")) ||
                (loan.NOR >= renewMAXMed && items.get(barcode).type.equals("Multimedia"))) {

            System.out.println("Sorry the maximum renews has been reached ;( ");
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(loan.dueDate);
        int renewPeriod = items.get(barcode).type.equals("Book") ? 14 : 7;
        cal.add(Calendar.DAY_OF_MONTH, renewPeriod);
        loan.dueDate = cal.getTime();
        loan.NOR++;

        System.out.println("this item has been renewed successfully !!!!!");
    }

// loading in the loans

    private void LibSysloadLoans() {
        File file = new File(LF);
        if (!file.exists()) return; // If the LOANS.csv doesn't exist yet, skip loading
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Date issueDate = sdf.parse(values[2].trim());
                Date dueDate = sdf.parse(values[3].trim());
                Loan loan = new Loan(values[0].trim(), values[1].trim(), issueDate, dueDate, Integer.parseInt(values[4].trim()));
                loans.add(loan);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //fetching the users

    private void LibSysloadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(userF))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                User user = new User(values[0].trim(), values[1].trim(), values[2].trim());
                users.put(user.userId, user);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    // fetching the items and sort then into 2 catagories

    private void LibLoadItems() {


        try (BufferedReader br = new BufferedReader(new FileReader(itemsF))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Items item;



                if (values[2].trim().equals("Book")) { // if its book then it will go to book class

                    item = new book(values[0].trim(), values[1].trim());
                } else {

                    item = new media(values[0].trim(), values[1].trim());
                }
                items.put(item.getBarcode(), item);
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
    }


}