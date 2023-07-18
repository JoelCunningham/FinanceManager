package com.financemanager.type;

import com.financemanager.JDBC;

/**
 * The Budget class represents a budget
 */
public class Budget extends CashCollection<BudgetItem> {
    
    /**
     * Constructs a new Budget object with the given month
     *
     * @param year The year of the month
     */
    public Budget(int month, int year) {
        super(month, year);
    }
    
    /**
     * Constructs a new Budget object with the given year
     *
     * @param year The year of the budget
     */
    public Budget(int year) {
        super(year);
    }

    /**
     * Constructs a new Budget object
     * 
     */
    public Budget() {
        super();
    }

    /**
     * Loads budget items from the specified year into the object
     */
    public void load() {
        JDBC jdbc = new JDBC();
        this.items = jdbc.getBudgetItems(this.year, this.month);
    }

    /**
     * Finds the value of a budget item
     *
     * @param category_id The id of the category of the budget item
     * @param span The span of the budget item (month or week number)
     * @return The amount of the budget item
     */
    public float findValue(int category_id, int span) {
        float value = 0;
        boolean year_mode = month == -1;
        for (BudgetItem item : items) {
            boolean valid_date = year_mode ? item.month == span : true;
            if (item.category_id == category_id && valid_date) {            
                value += year_mode ? item.amount : item.amount / 5;
            }
        }
        return value;
    }
}