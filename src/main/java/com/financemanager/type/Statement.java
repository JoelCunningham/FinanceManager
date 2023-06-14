package com.financemanager.type;

import java.time.LocalDate;

import com.financemanager.Helper;
import com.financemanager.JDBC;

/**
 * The Budget class represents a budget
 */
public class Statement extends CashCollection {
    public int month;
    public int year;
    public StatementItem[] items;

    /**
     * Constructs a new Statement object for a given month
     * 
     * @param month The month covered by the statement
     * @param year The year of the month
     */
    public Statement(int month, int year) {
        this.month = month;
        this.year = year;
        load();
    }

    /**
     * Constructs a new Statement object for a given year
     * 
     * @param year The year covered by the statement
     */
    public Statement(int year) {
        this.year = year;
        this.month = -1;
        load();
    }

    /**
     * Loads statement items from the specified year into the object
     *
     */
    public void load() {
        JDBC jdbc = new JDBC();
        this.items = jdbc.getStatementItems(this.year, this.month);
    }

    /**
     * Finds the week value of a Statement
     * 
     * @param category_id The id of the category of the statement item
     * @param week The week to compute
     * 
     * @return The total for the category in the given week
     */
    public float findValue(int category_id, int week) {
        float total = 0;
        for (StatementItem item : items) {
            LocalDate date = Helper.stringToDate(item.date);
            if (item.category_id == category_id && date.getDayOfMonth() / 7 + 1 == week) {
                total += item.amount;
            }
        }    
        return total;
    }

    /**
     * Finds the month value of a Statement
     *
     * @param category_id The id of the category of the statement item
     * @param week The month to compute
     * 
     * @return The total for the category in the given month
     */
    public float findMonthValue(int category_id, int month) {
        float total = 0;
        for (StatementItem item : items) {
            LocalDate date = Helper.stringToDate(item.date);
            if (date.getMonthValue() == month) {
                total += item.amount;
            }
        }    
        return total;
    }
}