package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                                                                                Press D to Deposits
                                                                                Press P to Payments
                                                                                Press R to Reports

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
    public static LocalTime localDate;
    public static FileWriter fileWriter;
    public static BufferedWriter bufferedWriter;
    public static DateTimeFormatter dateTimeFormatter;
    static void main(String[] args) {
        readFile(TRANSACTIONS_FILE);
        mainMenu();
        exit();
    }
    public static void displayLedger() {
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-30s %-30s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            System.out.printf("%-15s %-15s %-30s %-30s $%-10s%n", t.getDate(), t.getTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }
    public static void readFile(String fileName)  {
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

                Transaction transaction = new Transaction(date, time, description,vendor,amount);
                transactions.add(transaction);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Nope");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void mainMenu() {
        //Do while loop to keep the application working until boolean running = false
        boolean running = true;
        do {
            System.out.println(mainMenuPrompt);
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "D": //Add Deposit
                    addDeposit();
                    System.out.println("Success!");
                    break;
                case "P": //Make A Payment
                    addPayment();
                    System.out.println("Success!");
                    break;
                case "L"://Display ledger
                    running = false;
                    displayLedger();
                    subMenu();
                    break;
                case "X": //Exit
                    running = false;
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
    }

    public static void addPayment() {
        System.out.println("Enter date of deposit (MM/dd/yyyy): ");
        String paymentDate = readString();
        dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(paymentDate, dateTimeFormatter);

        System.out.println("Enter time of deposit (HH:mm:ss): ");
        String paymentTime = readString();
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(paymentTime, dateTimeFormatter);

        System.out.println("Enter Deposit Description: ");
        String description = readString();

        System.out.println("Enter Vendor Name: ");
        String vendorName = readString();
        /*
        made a defensive code for input if the user
        entered a negative amount, it's going to flag
         and will keep looping until user enters negative
        */
        double amount = 0;
        do {
            System.out.println("Amount: ");
            amount = readDouble();

            if (amount >= 0) {
                System.err.println("Invalid input! Payments must be negative.");
            }
        } while(amount >= 0);
        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendorName + "|" + amount);

            bufferedWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addDeposit() {
        //todo make sure the date is the same format in the file
        System.out.println("Enter date of deposit (MM/dd/yyyy): ");
        String depositDate = readString();
        dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(depositDate, dateTimeFormatter);
        //todo make sure time format is the same as in the file HH:mm:ss
        System.out.println("Enter time of deposit (HH:mm:ss): ");
        String depositTime = readString();
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.parse(depositTime, dateTimeFormatter);

        System.out.println("Enter Deposit Description: ");
        String description = readString();

        System.out.println("Enter Vendor Name: ");
        String vendorName = readString();

        /*
        made a defensive code for input if the user
        entered a positive amount, it's going to flag
         and will keep looping until user enters a positive amount
        */
        double amount = 0;
        do {
            System.out.println("Amount: ");
            amount = readDouble();

            if (amount <= 0) {
                System.err.println("Invalid input! Payments must be positive.");
            }
        } while(amount <= 0);
        try {
            fileWriter = new FileWriter(TRANSACTIONS_FILE, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.write(date + "|" + time + "|" + description + "|" + vendorName + "|" + amount);

            bufferedWriter.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void subMenu() {
        boolean running = true;
        //todo make a sub menu that has:
        // (A) Display all and it's sorted
        // (D) Display deposits
        // (P) Payments
        // (R) Reports
        do {
            System.out.println(subMenuPrompt);
            String input = readString();
            input = input.toUpperCase();

            switch (input) {
                case "A": //Add Deposit
                    System.out.println("this is working");
                    break;
                case "D": //Make A Payment
                    System.out.println("this is working");
                    break;
                case "P": //Display ledger
                    System.out.println("this is working");
                    break;
                case "R": //Report menu
                    running = false;
                    reportMenu();
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
    }

    public static void reportMenu() {
        boolean running = true;
        //todo make a sub menu that has:
        // (1) Month to Date
        // (2) Previous Month
        // (3) Year to Date
        // (4) Previous Year
        // (5) Search by inventory
        // (0) Back to subMenu()
        // (H) Home
        do {
            System.out.println(reportMenuPrompt);
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "1": //Month to Date
                    System.out.println("this is Month to Date");
                    break;
                case "2": //Previous Month
                    System.out.println("this is Previous Month");
                    break;
                case "3": //Year to Date
                    System.out.println("this is Year to Date");
                    break;
                case "4": //Previous Year
                    System.out.println("this is Previous Year");
                    break;
                case "5": //Search by Inventory
                    System.out.println("this is Search by Inventory");
                    break;
                case "0":
//                   Go back to subMenu()
                    break;
                case "H":
                    running = false; //Go back to mainMenu()
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
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

    public static int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    public static double readDouble() {
        return Double.parseDouble(scanner.nextLine());
    }
    /* endregion */
}
