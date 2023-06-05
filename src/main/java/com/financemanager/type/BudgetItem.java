package com.financemanager.type;

/**
 * The BudgetItem class represents a entry in a budget
 */
public class BudgetItem {

    public int category_id; 
    public int month;
    public float amount;

    /**
     * Constructs a new BudgetItem object with the given category, year, month and amount
     *
     * @param category_id The id of the category of the budget item
     * @param month The month of the budget item
     * @param amount The amount of the budget item
     */
    public BudgetItem(int category_id, int month, float amount) {
        this.category_id = category_id;
        this.month = month;
        this.amount = amount;
    }
}