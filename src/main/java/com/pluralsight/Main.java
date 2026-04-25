package com.pluralsight;

import java.util.Scanner;

public class Main {
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
    static void main(String[] args) {
        mainMenu();
        exit();
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
                case "D":
                    System.out.println("this is working");
                    break;
                case "P":
                    System.out.println("this is working");
                    break;
                case "L":
                    System.out.println("this is working");
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Wrong key! That rep doesn’t count.");
            }
        } while (!running);
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

    //region: Scanner for data types will be used in the capstone
    public static String readString() {
        return scanner.nextLine();
    }

    public static int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }

    public static double readDouble() {
        return Double.parseDouble(scanner.nextLine());
    }
    //end region for Scanner methods

}
