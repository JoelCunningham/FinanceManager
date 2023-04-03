package com.financemanager;

/**
 * The BudgetItem class represents a entry in a budget.
 */
public class BudgetItem {
    private int month;
    private int category; 
    private float amount;

    /**
     * Constructs a new BudgetItem object with the given category, year, month and amount.
     *
     * @param category The id of the category of the budget item.
     * @param month The month of the budget item.
     * @param amount The amount of the budget item.
     */
    public BudgetItem(int category, int month, float amount) {
        this.month = month;
        this.amount = amount;
        this.category = category;
    }

    /**
     * Returns the month of the budget item.
     *
     * @return The month of the budget item.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Returns the category of the budget item.
     *
     * @return The category of the budget item.
     */
    public int getCategory() {
        return category;
    }

    /**
     * Returns the amount of the budget item.
     *
     * @return The amount of the budget item.
     */
    public float getAmount() {
        return amount;
    }
}