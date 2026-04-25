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

    }

    public static void mainMenu() {
        System.out.println(mainMenuPrompt);
        String input = readString();
        input = input.toUpperCase();
        switch (input) {
            case "D":
                System.out.println("this is working");
        }
    }

    private static String readString() {
        return scanner.nextLine();
    }

    public static int readInt() {
        return Integer.parseInt(scanner.nextLine());
    }


}
