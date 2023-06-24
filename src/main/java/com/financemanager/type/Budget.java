package com.financemanager.type;

import com.financemanager.JDBC;

/**
 * The Budget class represents a budget
 */
public class Budget extends CashCollection<BudgetItem> {
    
    public int year;

    /**
     * Constructs a new Budget object with the given year
     *
     * @param year The year of the budget
     */
    public Budget(int year) {
        this.year = year;
        load();
    }

    /**
     * Loads budget items from the specified year into the object
     */
    public void load() {
        JDBC jdbc = new JDBC();
        this.items = jdbc.getBudgetItems(this.year);
    }

    /**
     * Finds the value of a budget item
     *
     * @param category_id The id of the category of the budget item
     * @param month The month of the budget item
     * 
     * @return The amount of the budget item.
     */
    public float findValue(int category_id, int month) {
        float value = 0;
        for (BudgetItem item : items) {
            if (item.month == month && item.category_id == category_id) {            
                value = item.amount;
                break;
            }
        }
        return value;
    }
}