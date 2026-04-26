package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final String fileName = "src/main/resources/transactions.csv";
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
                                                                               SQUATS AND SCIENCE BARBELL LLC
                                                                =============================================================
                                                                                 Please select an option:
    
                                                                                Press A to Display Ledger (Sorted)
                                                                                Press D to Deposits
                                                                                Press P to Payments
                                                                                Press R to Reports

            """;
    public static String reportMenuPrompt = """
                                                                =============================================================
                                                                               SQUATS AND SCIENCE BARBELL LLC
                                                                =============================================================
                                                                                 Please select an option:
    
                                                                                    Press 1 to Display Month to Date
                                                                                    Press 2 to Display Previous Month(s)
                                                                                    Press 3 to Display Year to Date
                                                                                    Press 4 to Display Previous Year
                                                                                    Press 5 to Search by inventory
                                                                                    Press 0 to Go Back
                                                                                    Press H to Go Back to Main Menu

            """;

    static void main(String[] args) {
        mainMenu();
//        fileReader(fileName);
    }

    public static void fileReader(String fileName)  {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = " ";
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitLine = line.split("\\|");
                line = line.trim();
                if(line.isEmpty()){
                    continue;
                }
                String date = splitLine[0];
                String time = splitLine[1];
                String description = splitLine[2];
                String vendor = splitLine[3];
                double amount = Double.parseDouble(splitLine[4]);

                Transaction transaction = new Transaction(date, time, description,vendor,amount);
                //todo make a method to display Arraylist<> transactions
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Nope");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void mainMenu() {
        //A prompt that will ask the user to pick on the main menu options displayed
        System.out.println(mainMenuPrompt);
        String input = readString();
        input = input.toUpperCase();

        //Do while loop to keep the application working until boolean running = false
        boolean running = true;
        do {
            switch (input) {
                case "D": //Add Deposit
                    System.out.println("this is working");
                    break;
                case "P": //Make A Payment
                    System.out.println("this is working");
                    break;
                case "L"://Display ledger
                    running = false;
                    subMenu();
                    break;
                case "X": //Exit
                    running = false;
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
        exit();
    }

    public static void subMenu() {
        boolean running = true;
        System.out.println(subMenuPrompt);
        String input = readString();
        //todo make a sub menu that has:
        // (A) Display all and it's sorted
        // (D) Display deposits
        // (P) Payments
        // (R) Reports
        do {
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
                case "R": //Exit
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
        exit();
    }

    public static void reportMenu() {
        boolean running = true;
        System.out.println(subMenuPrompt);
        String input = readString();
        //todo make a sub menu that has:
        // (1) Month to Date
        // (2) Previous Month
        // (3) Year to Date
        // (4) Previous Year
        // (5) Search by inventory
        // (0) Back to subMenu()
        // (H) Home
        do {
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
                case "R": //Exit
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
        exit();
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
