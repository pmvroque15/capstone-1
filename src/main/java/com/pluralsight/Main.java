package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static final String TRANSACTIONS_FILE = "src/main/resources/transactions.csv";
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    static void main(String[] args) {
        readFile(TRANSACTIONS_FILE);
        mainMenu();
        exit();
    }

    public static void displayHeader() {
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-30s %-30s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }

    public static void displayMainMenu() {
        System.out.println("""
                
                                                            =============================================================
                                                                            SQUATS AND SCIENCE BARBELL LLC
                                                            =============================================================
                                                                            Please select an option:
                
                                                                            Press D to Add Deposit
                                                                            Press P to Make a Payment
                                                                            Press L to Display Ledger
                                                                            Press X to Exit
                
                """);
    }

    public static void displayLedgerMenu() {
        System.out.println("""
                
                                                            =============================================================
                                                                                      LEDGER
                                                            =============================================================
                                                                             Please select an option:
                
                                                                            Press A to Display Ledger (Sorted)
                                                                            Press D to Display all Deposit Transactions
                                                                            Press P to Display all Payment Transactions
                                                                            Press R to Do a Custom Search Report
                                                                            Press H to Go Back
                
                """);
    }

    public static void displayCustomReportsMenu() {
        System.out.println("""
                
                                                            =============================================================
                                                                                     REPORTS
                                                            =============================================================
                                                                             Please select an option:
                
                                                                            Press 1 to Display Month to Date
                                                                            Press 2 to Display Previous Month(s)
                                                                            Press 3 to Display Year to Date
                                                                            Press 4 to Display Previous Year
                                                                            Press 5 to Search by Vendor
                                                                            Press 0 to Go Back
                                                                            Press H to Go Back to Main Menu
                
                
                """);
    }

    public static void displayLedger() {
        displayHeader();
        transactions.sort(Comparator.comparing(Transaction::getDate));
        transactions.sort(Comparator.comparing(Transaction::getTime));
        //todo make a variable
        for (Transaction t : transactions) {
            System.out.printf("%-15s %-15s %-30s %-30s $%-10s%n", t.getDate(), t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }

    public static void readFile(String fileName) {
        try {
            //clearing the transaction ArrayList so it won't multiply when method is called again
            transactions.clear();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = " ";
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                String[] splitLine = line.split("\\|");

                LocalDate date = LocalDate.parse(splitLine[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(splitLine[1], TIME_FORMATTER);
                String description = splitLine[2];
                String vendor = splitLine[3];
                double amount = Double.parseDouble(splitLine[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                //ArrayLists
                transactions.add(transaction);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found. It ghosted harder than your gym motivation.");
        } catch (IOException e) {
            System.err.println("I/O error. Something broke under pressure—too many reps.");
        }

    }

    // Main Menu
    public static void mainMenu() {
        //Do while loop to keep the application working until boolean running = false
        boolean isRunning = true;
        do {
            displayMainMenu();
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "D": //Add Deposit
                    Transaction deposit = getTransactionFromUser("Deposit");
                    addTransaction(deposit, TRANSACTIONS_FILE);
                    System.out.println("DEBUG: Success!");
                    break;
                case "P": //Make A Payment
                    Transaction payment = getTransactionFromUser("Payment");
                    addTransaction(payment, TRANSACTIONS_FILE);
                    System.out.println("DEBUG: Success!");
                    break;
                case "L"://Ledger menu
                    ledgerMenu();
                    break;
                case "X": //Exit
                    isRunning = false;
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (isRunning);
    }
    public static Transaction getTransactionFromUser(String transactionType) {
        LocalTime localTime;
        LocalDate localDate;

        //used ternary condition to make one if/else condition than doing every single input.

        System.out.printf("Enter date of %s (MM/dd/yyyy): \n", transactionType);
        String date = readString();
        localDate = LocalDate.parse(date, DATE_FORMATTER);

        System.out.printf("Enter time of %s (HH:mm:ss): \n", transactionType);
        String time = readString();
        localTime = LocalTime.parse(time, TIME_FORMATTER);

        System.out.printf("Enter %s Description: \n", transactionType);
        String description = readString();

        System.out.println("Enter Vendor Name: ");
        String vendorName = readString();

        /*
        made a defensive code and passed it through a method for input if the user
        entered a positive amount, it's going to flag
         and will keep looping until user enters a positive amount
        */
        double amount = readValidatedAmount(transactionType);

        return new Transaction(localDate, localTime, description, vendorName, amount);
    }

    public static void addTransaction(Transaction transaction, String fileName) {
            File file = new File(fileName);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
            if (file.length() > 0) {
                bufferedWriter.newLine();
            }

            bufferedWriter.write(transaction.getDate().format(DATE_FORMATTER) + "|" + transaction.getTime().format(TIME_FORMATTER) + "|" + transaction.getDescription() + "|" + transaction.getVendor() + "|" + transaction.getAmount());
            //to update the file since we append new data to transactions.csv
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("I/O error. Something broke under pressure—too many reps.");
        }
            //Re-reads the current file
            readFile(fileName);
    }

    public static double readValidatedAmount(String transactionType) {
        double amount;
        //Made a do while loop to make sure the user will enter the right amount whether it is deposit or payment.
        System.out.println("Amount: ");
        do {
            amount = readDouble();

            if ("Deposit".equalsIgnoreCase(transactionType) && amount < 0) {
                System.err.println("Invalid input! Deposit must be positive.");
            } else if ("Payment".equalsIgnoreCase(transactionType) && amount > 0) {
                System.err.println("Invalid input! Payments must be negative.");
            }
        } while (("Deposit".equalsIgnoreCase(transactionType) && amount <= 0) || ("Payment".equalsIgnoreCase(transactionType) && amount >= 0));

        return amount;
    }

    // Ledger Menu
    public static void ledgerMenu() {
        boolean isRunning = true;
        do {
            displayLedgerMenu();
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "A": //Display Ledger (sorted)
                    displayLedger();
                    break;
                case "D": //Display Deposit Transaction
                    displayTransactions(null, null, "Deposit");
                    break;
                case "P": //Display Payments Transactions
                    displayTransactions(null, null, "Payment");
                    break;
                case "R": //Custom Reports
                    customReportsMenu();
                    break;
                case "H":
                    isRunning = false; //back to Main Menu
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (isRunning);
    }

    //For this flexible method, used for Previous Month, Previous Year, YTD, and MTD
    public static void displayTransactions(LocalDate start, LocalDate end, String type) {
        boolean found = false;
        displayHeader();

        for (Transaction t : transactions) {
            LocalDate transactionDate = t.getDate();

            //in English: is it ON or AFTER start/ON or BEFORE end
            //Both null will pass everything otherwise, it will go through the filter process
            boolean matchesDate = (start == null || !transactionDate.isBefore(start)) && (end == null || !transactionDate.isAfter(end));
            //this way, it will be more clear whether it's a deposit or payment transaction, otherwise it will show all transactions
            boolean matchesType = false;
            //made an if/else condition because assigning it was too "strict" to filter the arguments for determining the transactions
            if (type == null) {
                matchesType = true;
            } else if ("Deposit".equalsIgnoreCase(type)) {
                matchesType = t.getAmount() > 0;
            } else if ("Payment".equalsIgnoreCase(type)) {
                matchesType = t.getAmount() < 0;
            }

            if (matchesDate && matchesType) {
                System.out.println(t);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions available this month.");
        }
    }

    //custom reports menu
    public static void customReportsMenu() {
        LocalDate TODAY = LocalDate.now();
        //last month's date from today
        LocalDate lastMonthDate = TODAY.minusMonths(1);
        //January 1st, 2026
        LocalDate JANUARY_FIRST = TODAY.withMonth(1).withDayOfMonth(1);
        //variables used for Previous Month
        //Last month's first day
        LocalDate firstOfLastMonth = lastMonthDate.withDayOfMonth(1);
        //Last month's last day
        LocalDate lastOfLastMonth = lastMonthDate.withDayOfMonth(lastMonthDate.lengthOfMonth());
        //variables used for Previous Year
        //January 1st of last year
        LocalDate firstOfLastYear = TODAY.minusYears(1).withMonth(1).withDayOfMonth(1);
        //Dec 31st of last year
        LocalDate lastOfLastYear = TODAY.minusYears(1).withMonth(12).withDayOfMonth(31);
        //todo make a sub menu that has:
        // (5) Search by inventory
        displayCustomReportsMenu();
        String input = readString();
        input = input.toUpperCase();

        switch (input) {
            case "1": //Month to Date
                displayTransactions(TODAY.withDayOfMonth(1), TODAY, null); //today.withDayOfMonth(1) = April 1st
                break;
            case "2": //Previous Month
                displayTransactions(firstOfLastMonth, lastOfLastMonth, null);
                break;
            case "3": //Year to Date
                //todo ask David if we have to follow the fiscal year
                displayTransactions(JANUARY_FIRST, TODAY, null);
                break;
            case "4": //Previous Year
                displayTransactions(firstOfLastYear, lastOfLastYear, null);
                break;
            case "5": //Search by Inventory
                displaySearchByInventory();
                break;
            case "0":
                //Go back to ledgerMenu()
                break;
            default:
                System.out.println("Wrong key! That rep doesn’t count.");
        }
    }

    private static void displaySearchByInventory() {
        boolean found = false;
        System.out.println("Search by vendor name: ");
        String vendor = readString().trim().toLowerCase();
        displayHeader();
        for (Transaction t : transactions) {
             if(t.getVendor().toLowerCase().contains(vendor)) {
                 System.out.println(t);
                found = true;
             }
        }

        if(!found) {
            System.out.println("No vendor found");
        }
    }

    public static void exit() {
        System.out.print("""
                   ____   __   __U _____ u\s
                U | __")u \\ \\ / /\\| ___"|/\s
                 \\|  _ \\/  \\ V /  |  _|"  \s
                  | |_) | U_|"|_u | |___  \s
                  |____/    |_|   |_____| \s
                 _|| \\\\_.-,//|(_  <<   >> \s
                (__) (__)\\_) (__)(__) (__)\s
                
                """);
    }

    /* region Scanner Methods */
    public static String readString() {
        return scanner.nextLine();
    }

    public static double readDouble() {
        return Double.parseDouble(scanner.nextLine());
    }
    /* endregion */
}