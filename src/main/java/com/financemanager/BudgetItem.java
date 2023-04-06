package com.financemanager;

/**
 * The BudgetItem class represents a entry in a budget.
 */
public class BudgetItem {

    public Category category; 
    public int month;
    public float amount;

    /**
     * Constructs a new BudgetItem object with default values.
     */
    public BudgetItem() {
        this(new Category(), 0, 0);
    }

    /**
     * Constructs a new BudgetItem object with the given category, year, month and amount.
     *
     * @param category The category of the budget item.
     * @param month The month of the budget item.
     * @param amount The amount of the budget item.
     */
    public BudgetItem(Category category, int month, float amount) {
        this.category = category;
        this.month = month;
        this.amount = amount;
    }
}