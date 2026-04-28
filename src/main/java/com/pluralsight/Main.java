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
    public static String mainMenuPrompt = """
            
                                                                =============================================================
                                                                               SQUATS AND SCIENCE BARBELL LLC
                                                                =============================================================
                                                                                Please select an option:
            
                                                                                Press D to Add Deposit
                                                                                Press P to Make a Payment
                                                                                Press L to Display Ledger
                                                                                Press X to Exit
            
            """;
    public static String subMenuPrompt = """
            
                                                                =============================================================
                                                                                         LEDGER
                                                                =============================================================
                                                                                 Please select an option:
            
                                                                                Press A to Display Ledger (Sorted)
                                                                                Press D to Display all Deposit Transactions
                                                                                Press P to Display all Payment Transactions
                                                                                Press R to Do a Custom Search Report
                                                                                Press H to Go Back
            
            """;
    public static String reportMenuPrompt = """
            
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
            
            
            """;
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    public static LocalTime localTime;
    public static LocalDate localDate;
    public static FileWriter fileWriter;
    public static BufferedWriter bufferedWriter;
    public static DateTimeFormatter dateTimeFormatter;
    public static LocalDate today = LocalDate.now();
    public static LocalDate lastMonthDate = today.minusMonths(1);
    public static LocalDate january = today.withMonth(1).withDayOfMonth(1);
    public static LocalDate firstOfLastMonth = lastMonthDate.withDayOfMonth(1);
    public static LocalDate lastOfLastMonth = lastMonthDate.withDayOfMonth(lastMonthDate.lengthOfMonth());


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

    public static void displayLedger() {
        displayHeader();
        transactions.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime)); //todo ask David
        for (Transaction t : transactions) {
            System.out.printf("%-15s %-15s %-30s %-30s $%-10s%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }

    public static void readFile(String fileName) {
        try {
            transactions.clear();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = " ";
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim(); //Defensive coding for whitespace
                String[] splitLine = line.split("\\|");

                String date = splitLine[0];
                String time = splitLine[1];
                String description = splitLine[2];
                String vendor = splitLine[3];
                double amount = Double.parseDouble(splitLine[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                //ArrayLists
                transactions.add(transaction);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Nope");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // main menu
    public static void mainMenu() {
        //Do while loop to keep the application working until boolean running = false
        boolean isRunning = true;
        do {
            System.out.println(mainMenuPrompt);
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "D": //Add Deposit
                    addTransaction(true);
                    System.out.println("Success!");
                    break;
                case "P": //Make A Payment
                    addTransaction(false);
                    System.out.println("Success!");
                    break;
                case "L"://Ledger menu
                    subMenu();
                    break;
                case "X": //Exit
                    isRunning = false;
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (isRunning);
    }

    public static void addTransaction(boolean isDeposit) {
        //used ternary condition to make one if/else condition than doing every single input.
        String type = isDeposit ? "Deposit" : "Payment";

        System.out.printf("Enter date of %s (MM/dd/yyyy): \n", type);
        String date = readString();
        dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        localDate = LocalDate.parse(date, dateTimeFormatter);

        System.out.printf("Enter time of %s (HH:mm:ss): \n", type);
        String time = readString();
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        localTime = LocalTime.parse(time, dateTimeFormatter);

        System.out.printf("Enter %s Description: \n", type);
        String description = readString();

        System.out.println("Enter Vendor Name: ");
        String vendorName = readString();

        /*
        made a defensive code and passed it through a method for input if the user
        entered a positive amount, it's going to flag
         and will keep looping until user enters a positive amount
        */
        double amount = readValidatedAmount(isDeposit);

        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendorName + "|" + amount);
            //to update the file since we append new data to transactions.csv
            bufferedWriter.close();
            //Re-reads the current file
            readFile(TRANSACTIONS_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double readValidatedAmount(boolean isDeposit) {
        double amount;
        //Made a do while loop to make sure the user will enter the right amount whether it is deposit or payment.
        System.out.println("Amount: ");
        do {
            amount = readDouble();

            if (isDeposit && amount < 0) {
                System.err.println("Invalid input! Deposit must be positive.");
            } else if (!isDeposit && amount > 0) {
                System.err.println("Invalid input! Payments must be negative.");
            }
        } while ((isDeposit && amount <= 0) || (!isDeposit && amount >= 0));

        return amount;
    }

    // sub menu
    public static void subMenu() {
        boolean isRunning = true;
        do {
            System.out.println(subMenuPrompt);
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "A": //Display Ledger (sorted)
                    displayLedger();
                    break;
                case "D": //Display Deposit Transaction
                    displayTransactions(null, null, true);
                    break;
                case "P": //Display Payments Transactions
                    displayTransactions(null, null, false);
                    break;
                case "R": //Custom Reports
                    reportMenu();
                    break;
                case "H":
                    isRunning = false; //back to Main Menu
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (isRunning);
    }

    public static void displayTransactions(LocalDate start, LocalDate end, Boolean isDeposit) {
        boolean found = false;
        displayHeader();

        for (Transaction t : transactions) {
            LocalDate transactionDate = LocalDate.parse(t.getDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));


            boolean matchesDate = (start == null || !transactionDate.isBefore(start)) && (end == null || !transactionDate.isAfter(end));
            boolean matchesType = isDeposit == null || (isDeposit && t.getAmount() > 0) || (!isDeposit && t.getAmount() < 0);

            if (matchesDate && matchesType) {
                System.out.printf("%-15s %-15s %-30s %-30s $%-10s%n", t.getDate(), t.getTime(),
                        t.getDescription(), t.getVendor(), t.getAmount());

                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions available this month.");
        }
    }

    //report menu
    public static void reportMenu() {
        //todo make a sub menu that has:
        // (2) Previous Month
        // (3) Year to Date
        // (4) Previous Year
        // (5) Search by inventory
        System.out.println(reportMenuPrompt);
        String input = readString();
        input = input.toUpperCase();

        switch (input) {
            case "1": //Month to Date
                displayTransactions(today.withDayOfMonth(1),today, null);
                break;
            case "2": //Previous Month
                displayTransactions(firstOfLastMonth, lastOfLastMonth, null);
                break;
            case "3": //Year to Date
                displayTransactions(january, today, null);
                break;
            case "4": //Previous Year
                System.out.println("this is Previous Year");
                break;
            case "5": //Search by Inventory
                System.out.println("this is Search by Inventory");
                break;
            case "0":
                //Go back to subMenu()
                break;
            default:
                System.out.println("Wrong key! That rep doesn’t count.");
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