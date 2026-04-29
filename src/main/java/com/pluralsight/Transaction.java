package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final LocalDate date; //Maybe change it into LocalDate data type?
    private final LocalTime time;
    private final String description;
    private final String vendor;
    private final double amount;
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //region getters
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }
    //endregion
    public String toString() {
        return  String.format("%-15s %-15s %-30s %-30s $%-10s ", this.date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")), this.time.format(DateTimeFormatter.ofPattern("HH:mm:ss")), this.description, this.vendor, this.amount);
    }
}
