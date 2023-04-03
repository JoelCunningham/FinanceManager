package com.financemanager;

/**
 * The Budget class represents a budget.
 */
public class Budget {
    private int year;
    private BudgetItem[] items;

    /**
     * Constructs a new Budget object with the given year and items array.
     *
     * @param year The year of the budget.
     * @param items The items array of the budget.
     */
    public Budget(int year, BudgetItem[] items) {
        this.year = year;
        this.items = items;
    }

    /**
     * Returns the year of the budget.
     *
     * @return The year of the budget.
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the items array of the budget.
     *
     * @return The items array of the budget.
     */
    public BudgetItem[] getItems() {
        return items;
    }
}