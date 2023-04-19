package com.financemanager.type;

import com.financemanager.JDBC;

/**
 * The Budget class represents a budget.
 */
public class Budget {
    public int year;
    public BudgetItem[] items;

    /**
     * Constructs a new Budget object with default values.
     */
    public Budget() {
        this(0, new BudgetItem[]{new BudgetItem()});
    }

    /**
     * Constructs a new Budget object with the given year and empty items array.
     *
     * @param year The year of the budget.
     * @param items The items array of the budget.
     */
    public Budget(int year, BudgetItem[] items) {
        this.year = year;
        this.items = items;
    }

    /**
     * Loads budget items form specified year into Budget object
     *
     * @param year The year of the budget to load. 
     */
    public void load(int year) {

        JDBC jdbc = new JDBC();

        this.year = year;
        this.items = jdbc.getBudgetItems(this.year);
    }

    /**
     * Finds the value of a budget item.
     *
     * @param category_id The id of the category of the budget item.
     * @param month The month of the budget item.
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