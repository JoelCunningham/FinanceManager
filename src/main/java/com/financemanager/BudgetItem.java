package com.financemanager;

/**
 * The BudgetItem class represents a entry in a budget.
 */
public class BudgetItem {
    public String header;
    public String category; 
    public int month;
    public float amount;

    /**
     * Constructs a new BudgetItem object with default values.
     */
    public BudgetItem() {
        this("", "", 0, 0);
    }

    /**
     * Constructs a new BudgetItem object with the given category, year, month and amount.
     *
     * @param header The name of the header of the budget item
     * @param category The name of the category of the budget item.
     * @param month The month of the budget item.
     * @param amount The amount of the budget item.
     */
    public BudgetItem(String header, String category, int month, float amount) {
        this.header = header;
        this.category = category;
        this.month = month;
        this.amount = amount;
    }
}