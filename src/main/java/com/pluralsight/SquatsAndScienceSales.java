package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class SquatsAndScienceSales {
    private static final String TRANSACTIONS_FILE = "src/main/resources/transactions.csv";
    private static ArrayList<Transaction> transactions = new ArrayList<>();
    public static Scanner scanner = new Scanner(System.in);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    static void main(String[] args) {
        transactions = readFile(TRANSACTIONS_FILE);
        mainMenu();
        exit();
    }

    public static ArrayList<Transaction> readFile(String fileName) {
        transactions.clear();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                String[] splitLine = line.split("\\|");

                LocalDate date = LocalDate.parse(splitLine[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(splitLine[1], TIME_FORMATTER);
                String description = splitLine[2];
                String vendor = splitLine[3];
                double amount = Double.parseDouble(splitLine[4]);

                Transaction t = new Transaction(date, time, description, vendor, amount);
                transactions.add(t);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found. It ghosted harder than your gym motivation.");
        } catch (IOException e) {
            System.err.println("I/O error. Something broke under pressure—too many reps.");
        }
        return transactions;
    }

    public static void displayHeader() {
        System.out.println("-------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-15s %-30s %-30s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------------------");
    }

    public static void displayMainMenu() {
        System.out.print("""
                                                         __  _  _ _  _ ___ __    _  _  _ __    __  __ _ ___ _  _  __ ___     _   _   __                       \s
                                                        / _|/ \\| | |/ |_ _/ _|  / \\| \\| |  \\  / _|/ _| | __| \\| |/ _| __|   | | | | / _|
                                {{{{{{{{                \\_ ( o | U | o | |\\_ \\ | o | \\\\ | o ) \\_ ( (_| | _|| \\\\ ( (_| _|    | |_| |( (_\s
                                I ^  ^ I                |__/\\_,|___|_n_|_||__/ |_n_|_|\\_|__/  |__/\\__|_|___|_|\\_|\\__|___()  |___|___\\__|
                               CI @  @ ID                                                                               V             \s
                            __  I  .L  I  __                 =============================================================
                         _I  I \\  ~~  / I  I_                                         MAIN MENU
                         I I  I  ______  I  I I              =============================================================
                   []    I I__I          I__I I    []                           Please select an option:
                  [ ]    I I  Io        oI  I I    [ ]                          Press D to Add Deposit
                 [  ]======OOOO==========OOOO======[  ]                         Press P to Make a Payment
                  [ ]    I___I__\\      /__I___I    [ ]                          Press L to Display Ledger
                   []    (______)      (_______)   []                           Press X to Exit                                                                                \s
                \s""");
    }

    public static void displayLedgerMenu() {
        System.out.println("""
                
                                                             =============================================================  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⢤⡖⠺⠉⠓⠢⣄⠀⠀⠀⠀⠀⠀⠀
                                                                                        LEDGER                                     ⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⢞⣿⣿⣭⣟⣯⣾⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⢸⣅⠉⠀⢻⣦⠀⡀⠘⣆⠀⠀⠀⠀⠀⠀
                                                             =============================================================      ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣟⣿⡿⣿⣿⣿⢟⣿⣿⠟⢿⡀⠀⠀⠀⠀⠀⠀⠀⢟⣿⣾⣿⣿⣿⣇⠀⢡⠘⣆⠀⠀⠀⠀⠀
                                                                                Please select an option:                                     ⣿⣿⣿⣿⣿⣶⣿⣻⡿⠁⠀⠀⢣⠀⠀⠀⠀⠀⠀⠀⠀⠙⠛⠉⠉⠁⢻⠈⡆⢳⡈⢳⡀⠀⠀⠀
                                                                           Press A to Display Ledger (Sorted)                              ⠸⢿⣿⣿⣿⣿⣽⣿⡏⠀⠐⠾⣿⣿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡇⠟⢰⡥⠀⢝⣄⠀⠀
                                                                           Press D to Display all Deposit Transactions                   ⠀⠀⠈⣿⣿⣿⣷⡘⠃⠀⠀⠀⠀⠙⢁⣱⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢼⢣⠞⣀⢇⠈⠱⠚⣆⠀
                                                                           Press P to Display all Payment Transactions                     ⠀⢰⣿⢿⣻⣿⣿⣿⡅⠀⠀⠀⢦⣬⡇⠀⠀⠀⠀⠀⠀⠀⢠⠚⡏⠉⠑⢺⡄⠀⠀⠙⣧⡀⠇⠀⡇
                                                                           Press R to Do a Custom Search Report                            ⢀⡾⣷⠈⠉⠙⠛⠿⢿⣷⣦⣄⢰⣾⠖⣊⣉⡩⠍⢉⠓⠶⣿⢁⠜⢇⠁⢀⣹⣷⣤⣤⣈⣇⠀⣸⢧
                                                                           Press H to Go Back to the Main Menu                         ⠀⣀⡴⢛⡇⠉⠀⠀⡀⢀⡀⠀⠀⠉⢙⡏⠁⠀⢹⣇⡀⠙⣏⠢⡌⡉⠉⣒⡷⠚⠉⠉⢻⣿⣿⣿⣵⣾⣷⣾
                                                                                                                           ⠀⠀⠀⠀⠀⠀⠀⠀⢀⡤⠚⠁⢀⣼⠋⣿⡅⠀⠀⠀⠀⠈⠉⠓⣦⡨⠀⡀⠀⠀⢈⣉⡒⠒⣶⡶⠂⠉⠀⠠⣤⣴⣶⣾⣿⣿⠿⠛⠉⠁
                                                                                                                           ⠀⠀⠀⠀⠀⠀⠀⣴⠋⠉⠙⠋⠉⢸⣥⡤⠜⠋⢤⣦⢤⣤⣴⡾⠟⠁⠀⠙⢒⣫⣥⣴⣶⣿⣏⠀⠉⠛⠿⢿⣿⣿⣿⡿⠋⠁⠀⠀⠀⠀
                                                                                                                           ⠀⠀⠀⠀⢀⡤⠚⠙⣷⣿⣦⡀⠀⢨⡏⠀⠀⠀⠀⠀⠀⠀⣩⠀⠀⠀⠉⠉⠉⢉⡛⢻⣿⣿⣿⣷⣶⣶⣶⣶⡿⠛⠁⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⠀⠀⣰⠏⢀⠀⠀⣖⠈⠁⠉⠙⢻⣷⣄⣀⣤⣤⡴⠿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠈⢻⣿⣿⣿⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⠀⠀⡏⢰⡟⠀⠀⣿⡄⠀⠀⠀⣿⠀⠀⠀⠀⠀⢀⣴⠟⠁⢿⣄⣀⡀⠀⣀⣤⣶⣿⣿⣾⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⠀⣸⠀⢸⡁⠀⠀⠸⣿⣄⠀⡀⣿⠀⠀⡠⣶⡷⠋⡀⠀⠀⠚⠛⠛⠛⠛⠛⠛⠃⠈⠑⡿⢸⣯⡝⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⢠⠇⠀⠘⣿⣦⣤⣤⣿⡟⠛⠓⢿⣞⠻⠟⣔⠲⡇⣀⡀⠀⠀⠀⠀⠀⠀⠀⠐⠀⠀⢠⣾⣺⠟⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⢸⠀⠀⠀⣿⣿⠉⠛⠿⢦⣄⡠⠘⡆⣀⣤⠀⠀⠀⢐⣮⠗⠃⠋⠛⠛⠛⠛⠛⢻⣿⡿⡍⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⣼⠀⠀⠀⢸⣿⠀⠀⠀⠀⠀⠭⠌⣧⢾⣧⣤⣾⣦⠥⢠⣀⣀⢄⣠⣦⣶⣾⣿⣿⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⣿⠀⣀⣴⣿⣿⣿⣿⣦⡂⠀⠀⠀⣾⣙⡇⠀⠀⠀⠀⠀⠀⠁⠀⣡⣿⣿⣿⣿⣯⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⣿⡿⢋⣡⣾⣿⣿⣟⣻⠿⣿⠷⣤⣿⣿⣇⠀⠀⠀⠀⠀⠠⣀⣿⣿⣿⠛⠛⠻⠏⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⢰⡟⣻⣿⣿⣿⣿⣿⣼⡏⠀⠈⠑⢤⣹⡿⣿⣯⠻⢿⣿⣿⣿⣽⠿⢟⣃⣀⣀⡨⣏⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⣾⣾⠁⠀⠈⢹⣿⣿⠟⠀⠀⠀⠀⠀⠈⠛⢾⣿⡆⣶⣿⣿⠗⠒⢉⣉⣉⣙⣛⢿⣧⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⢹⣿⠀⠀⢷⡀⢻⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⣷⣿⣿⣷⣿⣿⣿⣿⣿⣟⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠈⠿⣄⠀⣸⣿⣄⣻⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣏⣉⣉⣉⣉⣉⣿⣏⣉⣉⣉⣉⣉⣉⣙⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                                                                                                           ⠀⠀⠀⠙⢷⣌⠧⠈⡇⠀⠀⠀⠀⠀⠀⠀⠀⢠⡟⣟⣏⣿⣷⡇⢸⣿⣿⣿⣿⣿⠆⣿⠀⠿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                """);
    }

    public static void displayCustomReportsMenu() {
        System.out.println("""
                
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⢴⡄⠀⣾⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀=============================================================
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⠃⣰⣧⣼⣿⣽⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀               REPORTS
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣇⠀⣿⠿⢿⠻⠿⣿⣷⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀=============================================================
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣾⠿⠢⠄⠁⠀⣬⣿⢿⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀        Please select an option:
                ⠀⠀⠀⠀⠀⠀⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⡁⠀⠀⢀⠠⠧⠄⠈⣧⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⠀Press 1 to Display Month to Date
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠓⢄⣀⠀⠀⣀⣀⠜⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   Press 2 to Display Previous Month(s)
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡏⠉⠠⣿⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   Press 3 to Display Year to Date
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡖⠉⠑⠀⠲⣤⠤⠘⠉⡁⠲⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   Press 4 to Display Previous Year
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡏⠀⡀⠀⡄⠀⠰⠀⠀⠀⢹⡀⢱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   Press 5 to Search by Vendor
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡏⠀⡀⠀⡄⠀⠰⠀⠀⠀⢹⡀⢱⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   Press 6 to Custom Search
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡀⢲⢧⣠⣀⣀⠴⢄⣀⣤⣆⠘⣼⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   Press 0 to Go Back
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⡇⠎⡾⣄⣀⣀⣴⠒⢄⣀⣿⠀⢺⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⡇⢰⣷⣖⠁⠁⢈⡀⠣⣾⢻⣄⡜⣣⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣴⠁⢳⢉⡏⠃⠠⡀⠀⡏⠉⣼⠸⠙⡀⢹⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⠀⠀⢰⠟⣄⢰⠈⢹⠉⢰⣿⣆⡆⠱⣾⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                ⠀⠀⠀⣀⡤⡔⠊⠉⡱⢆⠀⠀⠀⠀⠀⠀⠀⢸⠀⢀⡞⠀⠈⠳⡶⢼⠤⡿⠁⠘⣷⠀⡀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣄⣀⠀⠀⠀⠀⠀⠀
                ⢀⡔⢺⠁⠀⡇⠀⠀⢷⠈⢣⠀⠀⠀⠀⠀⢀⣼⢦⣼⣁⣀⠤⠤⠷⠤⠼⠥⠤⢤⣻⣟⣻⡀⠀⠀⠀⠀⠀⡴⠋⠀⢸⠈⠹⠒⢄⡀⠀⠀
                ⢻⠀⢸⠀⠀⢧⠀⠀⠸⣷⡞⢧⢀⡠⠤⠒⠛⣿⡓⣛⣧⣤⠒⡶⢶⣲⡶⠒⠶⣶⠏⠨⣹⣗⠒⠢⠤⣀⡎⠀⠀⠀⡘⠀⢠⠀⠈⡏⢳⡄
                ⢸⠀⠀⡆⠀⠸⡄⠀⠀⢣⣤⠊⢁⡠⠤⠒⠉⠻⢿⡿⠋⠋⠐⡅⠀⣿⠀⠀⠰⠉⠚⣿⠋⠀⠉⠑⠢⡤⣉⣲⠀⢠⠃⠀⡜⠀⢠⠀⠸⢸
                ⠀⡆⠀⢹⡀⠀⢳⠀⠀⠈⢏⠋⢳⠀⠀⠀⠀⠀⠸⣇⢄⡖⠀⢸⢸⢿⠰⠀⠀⡟⢰⠇⠀⠀⠀⠀⣸⠀⠀⠀⣰⠏⠀⣰⠃⢀⡎⢀⠇⡸
                ⡀⠹⡄⠀⢷⠀⠀⢧⠀⠀⠈⡆⠀⡇⠀⠀⠀⠀⠀⢹⡟⠑⢤⣾⠋⠀⢳⣀⠘⠙⠇⠀⠀⠀⠀⠀⡟⠀⢀⣼⠋⠀⡰⠃⠀⡜⠀⡜⢀⠃
                ⢣⡀⢹⣆⡀⢳⣀⠀⠳⣄⡀⠘⢦⡕⠀⠀⠀⠀⠀⢸⡇⠀⠀⢳⠀⠀⣼⠉⠀⢸⡆⠀⠀⠀⠀⠀⠺⣶⡚⠁⣠⠖⠁⣠⠎⢀⡜⣠⠊⠀
                """);
    }

    public static void displayLedger() {
        displayHeader();
        transactions.sort(Comparator.comparing(Transaction::getTime).reversed());
//        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public static void mainMenu() {
        boolean isRunning = true;
        do {
            displayMainMenu();
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "D":
                    /* run the method getTransactionFromUser then build me a new Transaction object and then store it in variable deposit */
                    Transaction deposit = getTransactionFromUser("Deposit");
                    //After making the Transaction Object, I want to pass it to addTRansactions, that way it writes it in the csv file.
                    addTransaction(deposit, TRANSACTIONS_FILE);
                    System.out.println("Funds added. Your account is now bulking season ready.");
                    break;
                case "P":
                    Transaction payment = getTransactionFromUser("Payment");
                    addTransaction(payment, TRANSACTIONS_FILE);
                    System.out.println("Payment successful. No spotter needed for that financial rep.");
                    break;
                case "L":
                    ledgerMenu();
                    break;
                case "X":
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

        System.out.println("Amount: ");
        double amount = readValidatedAmount(transactionType);

        return new Transaction(localDate, localTime, description, vendorName, amount);
    }

    public static void addTransaction(Transaction transaction, String fileName) {
        File file = new File(fileName);
        try  {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
            //if the file has data, then go to the next line and write there
            if (file.length() > 0) {
                bufferedWriter.newLine();
            }

            bufferedWriter.write(transaction.getDate().format(DATE_FORMATTER) + "|" + transaction.getTime().format(TIME_FORMATTER) + "|" + transaction.getDescription() + "|" + transaction.getVendor() + "|" + transaction.getAmount());
            bufferedWriter.close();
            transactions.add(transaction);
        } catch (IOException e) {
            System.err.println("I/O error. Something broke under pressure—too many reps.");
        }
    }

    public static void customFilters() {
        ArrayList<Transaction> results = new ArrayList<>();

        System.out.println("Enter start date (MM/dd/yyyy) or press Enter to skip: ");
        String startDate = readString().trim();

        System.out.println("Enter end date (MM/dd/yyyy) or press Enter to skip: ");
        String endDate = readString().trim();

        System.out.println("Enter Description or press Enter to skip: ");
        String description = readString().trim();

        System.out.println("Enter Vendor Name or press Enter to skip: ");
        String vendorName = readString().trim();

        LocalDate start = null;
        LocalDate end = null;

        if (!startDate.isBlank()) {
            start = LocalDate.parse(startDate, DATE_FORMATTER);
        }

        if (!endDate.isBlank()) {
            end = LocalDate.parse(endDate, DATE_FORMATTER);
        }

        for (Transaction t : transactions) {
            boolean matchesDescription = t.getDescription().toLowerCase().contains(description.toLowerCase())
                    || description.isBlank();

            boolean matchesVendor = t.getVendor().toLowerCase().contains(vendorName.toLowerCase()) || vendorName.isBlank();
            boolean matchesWithinTheDateRange = (start == null || !t.getDate().isBefore(start)) && (end == null || !t.getDate().isAfter(end));


            if (matchesWithinTheDateRange && matchesVendor && matchesDescription) {
                results.add(t);
            }
        }
        displayHeader();
        for (Transaction r: results) {
            System.out.println(r);
        }

        if (results.isEmpty()) {
            System.out.println("No transactions found.");
        }
    }

    public static double readValidatedAmount(String transactionType) {
        double amount;
        do {
            amount = readDouble();

            if ("Deposit".equalsIgnoreCase(transactionType) && amount < 0) {
                System.err.println("Invalid input! Deposit must be positive.");
            } else if ("Payment".equalsIgnoreCase(transactionType) && amount > 0) {
                System.err.println("Invalid input! Payment must be negative.");
            }
        } while (("Deposit".equalsIgnoreCase(transactionType) && amount <= 0) || ("Payment".equalsIgnoreCase(transactionType) && amount >= 0));

        return amount;
    }

    public static void ledgerMenu() {
        boolean isRunning = true;
        do {
            displayLedgerMenu();
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayTransactions(null, null, "Deposit");
                    break;
                case "P":
                    displayTransactions(null, null, "Payment");
                    break;
                case "R":
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
            boolean matchesDate = (start == null || !transactionDate.isBefore(start)) && (end == null || !transactionDate.isAfter(end));
            //this way, it will be more clear whether it's a deposit or payment transaction, otherwise it will show all transactions
            boolean matchesType = false;
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
            System.out.println("No transactions this month—your ledger is lighter than your warm-up set.");
        }
    }

    public static void customReportsMenu() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonthDate = today.minusMonths(1);
        LocalDate january1st = today.withDayOfYear(1);
        LocalDate firstOfLastMonth = lastMonthDate.withDayOfMonth(1);
        LocalDate lastOfLastMonth = lastMonthDate.withDayOfMonth(lastMonthDate.lengthOfMonth());
        LocalDate firstOfLastYear = today.minusYears(1).withMonth(1).withDayOfMonth(1);
        LocalDate lastOfLastYear = today.minusYears(1).withMonth(12).withDayOfMonth(31);

        boolean running = true;

        do {
            displayCustomReportsMenu();
            String input = readString();
            input = input.toUpperCase();
            switch (input) {
                case "1": //Month to Date
                    displayTransactions(today.withDayOfMonth(1), today, null); //today.withDayOfMonth(1) = April 1st
                    break;
                case "2": //Previous Month
                    displayTransactions(firstOfLastMonth, lastOfLastMonth, null);
                    break;
                case "3": //Year to Date
                    displayTransactions(january1st, today, null);
                    break;
                case "4": //Previous Year
                    displayTransactions(firstOfLastYear, lastOfLastYear, null);
                    break;
                case "5": //Search by Inventory
                    displaySearchByVendor();
                    break;
                case "6":
                    customFilters();
                    break;
                case "0":
                    //Go back to ledgerMenu()
                    running = false;
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (running);
    }

    private static void displaySearchByVendor() {

        boolean found = false;
        System.out.println("Search by vendor name: ");
        String vendor = readString().trim().toLowerCase();
        displayHeader();
        for (Transaction t : transactions) {
            if (t.getVendor().toLowerCase().contains(vendor)) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No vendor found");
        }
    }

    public static void exit() {
        System.out.print("""
                                                          ____  _     _    _____ _____ _  _      _____   ____  ____  _      _          \s
                                                         / ___\\/ \\ /|/ \\ /Y__ __Y__ __Y \\/ \\  /|/  __/  /  _ \\/  _ \\/ \\  /|/ \\  /|     \s
                                                         |    \\| |_||| | || / \\   / \\ | || |\\ ||| |  _  | | \\|| / \\|| |  ||| |\\ ||     \s
                                                         \\___ || | ||| \\_/| | |   | | | || | \\||| |_//  | |_/|| \\_/|| |/\\||| | \\||______
                                                         \\____/\\_/ \\|\\____/ \\_/   \\_/ \\_/\\_/  \\|\\____\\  \\____/\\____/\\_/  \\|\\_/  \\|\\/\\/\\/
                                                    \s
                                                            \s
                \s""");

        System.out.print("""
                             _____ _     ____  _      _  __  ___  _ ____  _       _____ ____  ____    _     ____  _  _      _____   _____ _     _  ____    ____  ____  ____  _\s
                            /__ __Y \\ /|/  _ \\/ \\  /|/ |/ /  \\  \\///  _ \\/ \\ /\\  /    //  _ \\/  __\\  / \\ /\\/ ___\\/ \\/ \\  /|/  __/  /__ __Y \\ /|/ \\/ ___\\  /  _ \\/  __\\/  __\\/ \\
                              / \\ | |_||| / \\|| |\\ |||   /    \\  / | / \\|| | ||  |  __\\| / \\||  \\/|  | | |||    \\| || |\\ ||| |  _    / \\ | |_||| ||    \\  | / \\||  \\/||  \\/|| |
                              | | | | ||| |-||| | \\|||   \\    / /  | \\_/|| \\_/|  | |   | \\_/||    /  | \\_/|\\___ || || | \\||| |_//    | | | | ||| |\\___ |  | |-|||  __/|  __/\\_/
                              \\_/ \\_/ \\|\\_/ \\|\\_/  \\|\\_|\\_\\  /_/   \\____/\\____/  \\_/   \\____/\\_/\\_\\  \\____/\\____/\\_/\\_/  \\|\\____\\    \\_/ \\_/ \\|\\_/\\____/  \\_/ \\|\\_/   \\_/   (_)
                
                """);
    }

    public static String readString() {
        return scanner.nextLine();
    }

    public static double readDouble() {
        return Double.parseDouble(scanner.nextLine());
    }
}