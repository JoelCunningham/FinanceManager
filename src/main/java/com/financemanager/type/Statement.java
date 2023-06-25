package com.financemanager.type;

import java.time.LocalDate;

import com.financemanager.Helper;
import com.financemanager.JDBC;

/**
 * The Budget class represents a budget
 */
public class Statement extends CashCollection<StatementItem> {
    
    public int month;
    public int year;

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
     * Finds the value of a statement item
     *
     * @param category_id The id of the category of the statement item
     * @param month The month of the statement item
     * @param size The size of to spilt into
     * @return The amount of the statement item
     */
    public float findValue(int category_id, int span, int size) {
        float total = 0;
        for (StatementItem item : items) {
            LocalDate date = Helper.stringToDate(item.date);
            boolean valid_date = size == 12 ? date.getMonthValue() == span : date.getDayOfMonth() / 7 + 1 == span;

            if (item.category_id == category_id && valid_date) {
                total += item.amount;
            }
        }    
        return total;
    }

}