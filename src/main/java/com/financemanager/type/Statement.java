package com.financemanager.type;

import java.time.LocalDate;

import com.financemanager.Helper;
import com.financemanager.JDBC;

/**
 * The Statement class represents a statement
 */
public class Statement extends CashCollection<StatementItem> {

    /**
     * Constructs a new Statement object for a given month
     * 
     * @param month The month covered by the statement
     * @param year The year of the month
     */
    public Statement(int month, int year) {
        super(month, year);
    }

    /**
     * Constructs a new Statement object for a given year
     * 
     * @param year The year covered by the statement
     */
    public Statement(int year) {
        super(year);
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
     * @param span The span of the statement item (month or week number)
     * @return The amount of the statement item
     */
    public float findValue(int category_id, int span) {
        float value = 0;
        boolean year_mode = month == -1;
        for (StatementItem item : items) {
            LocalDate date = Helper.stringToDate(item.date);
            boolean valid_date = year_mode ? date.getMonthValue() == span : date.getDayOfMonth() / 7 + 1 == span;
            if (item.category_id == category_id && valid_date) {
                value += item.amount;
            }
        }    
        return value;
    }

}